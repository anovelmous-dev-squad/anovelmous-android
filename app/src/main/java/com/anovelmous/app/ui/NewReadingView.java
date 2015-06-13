package com.anovelmous.app.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.R;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * Created by Greg Ziegan on 6/12/15.
 */
public class NewReadingView extends FrameLayout implements ObservableScrollViewCallbacks {

    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.scrollable) ObservableScrollView scrollView;
    @InjectView(R.id.fab) View mFab;

    private final int mFabMargin;
    private boolean mFabIsShown;

    public NewReadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            AnovelmousApp.get(context).inject(this);
        }

        mFabMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mFabIsShown = true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        //((AppCompatActivity) getContext()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu_icon);

        scrollView.setScrollViewCallbacks(this);

        ScrollUtils.addOnGlobalLayoutListener(mFab, new Runnable() {
            @Override
            public void run() {
                float fabTranslationY = getHeight() - mFabMargin - mFab.getHeight();
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                    // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
                    // which causes FAB's OnClickListener not working.
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
                    lp.leftMargin = getWidth() - mFabMargin - mFab.getWidth();
                    lp.topMargin = (int) fabTranslationY;
                    mFab.requestLayout();
                } else {
                    mFab.setTranslationX(getWidth() - mFabMargin - mFab.getWidth());
                    mFab.setTranslationY(fabTranslationY);
                }
            }
        });
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Timber.d("DEBUG: onUpOrCancelMotionEvent: " + scrollState);
        if (scrollState == ScrollState.UP) {
            hideFab();
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            showFab();
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return toolbar.getTranslationY() == 0;
    }

    private boolean toolbarIsHidden() {
        return toolbar.getTranslationY() == -toolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-toolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (toolbar.getTranslationY() == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(toolbar.getTranslationY(), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                toolbar.setTranslationY(translationY);
                (scrollView).setTranslationY(translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) (scrollView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                (scrollView).requestLayout();
            }
        });
        animator.start();
    }

    private int getScreenHeight() {
        return getRootView().getHeight();
    }

    private void showFab() {
        if (!mFabIsShown) {
            mFab.animate().cancel();
            mFab.animate().scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            mFab.animate().cancel();
            mFab.animate().scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }
}

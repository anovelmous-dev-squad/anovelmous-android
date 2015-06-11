package com.anovelmous.app.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.anovelmous.app.R;
import com.anovelmous.app.ui.reading.ReadingBaseActivity;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;

/**
 * Created by Greg Ziegan on 6/11/15.
 */
public abstract class ToolbarControlBaseActivity<S extends Scrollable> extends ReadingBaseActivity
        implements ObservableScrollViewCallbacks {
    private Toolbar mToolbar;
    private S mScrollable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mScrollable = createScrollable();
        mScrollable.setScrollViewCallbacks(this);
    }

    protected abstract int getLayoutResId();

    protected abstract S createScrollable();

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Log.e("DEBUG", "onUpOrCancelMotionEvent: " + scrollState);
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return mToolbar.getTranslationY() == 0;
    }

    private boolean toolbarIsHidden() {
        return mToolbar.getTranslationY() == -mToolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (mToolbar.getTranslationY() == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(mToolbar.getTranslationY(), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                mToolbar.setTranslationY(translationY);
                ((View) mScrollable).setTranslationY(translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) mScrollable).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) mScrollable).requestLayout();
            }
        });
        animator.start();
    }
}

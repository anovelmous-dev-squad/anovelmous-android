package com.anovelmous.app.ui;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anovelmous.app.R;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.Scrollable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

/**
 * Created by Greg Ziegan on 6/18/15.
 */
public abstract class ToolbarControlBaseActivity<S extends Scrollable> extends BaseActivity implements ObservableScrollViewCallbacks {

    private FrameLayout container;
    private ViewGroup content;
    private Toolbar toolbar;
    private S scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);

        content = (ViewGroup) findViewById(R.id.main_content);

        inflater.inflate(R.layout.activity_main, content);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        container = (FrameLayout) findViewById(R.id.container);
        inflater.inflate(R.layout.fragment_reading, container);

        scrollView = createScrollable();
        scrollView.setScrollViewCallbacks(this);
    }

    protected Toolbar getToolbar() {
        return toolbar;
    }

    protected abstract S createScrollable();

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Timber.d("DEBUG: onUpOrCancelMotionEvent: " + scrollState);
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
                ((View) scrollView).setTranslationY(translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) scrollView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) scrollView).requestLayout();
            }
        });
        animator.start();
    }
}

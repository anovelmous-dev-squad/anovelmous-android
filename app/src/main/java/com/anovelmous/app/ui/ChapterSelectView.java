package com.anovelmous.app.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.R;
import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.Order;
import com.anovelmous.app.data.api.Sort;
import com.anovelmous.app.data.api.model.Chapter;
import com.anovelmous.app.data.api.model.ChaptersResponse;
import com.anovelmous.app.data.api.model.NovelsResponse;
import com.anovelmous.app.ui.misc.BetterViewAnimator;
import com.anovelmous.app.ui.misc.DividerItemDecoration;
import com.anovelmous.app.ui.trending.TrendingTimespan;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.anovelmous.app.ui.misc.DividerItemDecoration.VERTICAL_LIST;

/**
 * Created by Greg Ziegan on 6/2/15.
 */
public class ChapterSelectView extends LinearLayout
        implements SwipeRefreshLayout.OnRefreshListener, ChapterClickListener {

    @InjectView(R.id.trending_toolbar) Toolbar toolbarView;
    @InjectView(R.id.trending_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.trending_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.trending_list) RecyclerView chaptersView;
    @InjectView(R.id.trending_loading_message) TextView loadingMessageView;

    @Inject AnovelmousService anovelmousService;

    private final float dividerPaddingStart;
    private final PublishSubject<Chapter> chapterIndexSubject;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    public ChapterSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            AnovelmousApp.get(context).inject(this);
        }

        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);
        chapterIndexSubject = PublishSubject.create();
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        AnimationDrawable ellipsis =
                (AnimationDrawable) getResources().getDrawable(R.drawable.dancing_ellipsis);
        loadingMessageView.setCompoundDrawablesWithIntrinsicBounds(null, null, ellipsis, null);
        ellipsis.start();

        toolbarView.setNavigationIcon(R.drawable.menu_icon);
        toolbarView.setNavigationOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) {
                // TODO bind to drawer with... injection?
            }
        });

        swipeRefreshView.setColorSchemeResources(R.color.accent);
        swipeRefreshView.setOnRefreshListener(this);
        chaptersView.setLayoutManager(new LinearLayoutManager(getContext()));
        chaptersView.addItemDecoration(
                new DividerItemDecoration(getContext(), VERTICAL_LIST, dividerPaddingStart, safeIsRtl()));
        //chaptersView.setAdapter(trendingAdapter);
    }

    private boolean safeIsRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        subscriptions.add(chapterIndexSubject
            .subscribe());
        onRefresh();
    }

    @Override
    public void onChapterClick(Chapter chapter) {

    }

    @Override
    public void onRefresh() {

    }
}

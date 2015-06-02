package com.anovelmous.app.ui.trending;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.R;
import com.anovelmous.app.data.IntentFactory;
import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.Order;
import com.anovelmous.app.data.api.Sort;
import com.anovelmous.app.data.api.model.Novel;
import com.anovelmous.app.data.api.model.NovelsResponse;
import com.anovelmous.app.data.api.transforms.SearchResultToNovelList;
import com.anovelmous.app.ui.misc.BetterViewAnimator;
import com.anovelmous.app.ui.misc.DividerItemDecoration;
import com.anovelmous.app.ui.misc.EnumAdapter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.anovelmous.app.ui.misc.DividerItemDecoration.VERTICAL_LIST;

/**
 * Created by Greg Ziegan on 6/1/15.
 */
public class TrendingView extends LinearLayout
        implements SwipeRefreshLayout.OnRefreshListener, TrendingAdapter.NovelClickListener {
    @InjectView(R.id.trending_toolbar) Toolbar toolbarView;
    @InjectView(R.id.trending_timespan) Spinner timespanView;
    @InjectView(R.id.trending_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.trending_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.trending_list) RecyclerView novelsView;
    @InjectView(R.id.trending_loading_message) TextView loadingMessageView;

    @Inject AnovelmousService anovelmousService;
    @Inject IntentFactory intentFactory;

    private final float dividerPaddingStart;


    private final PublishSubject<TrendingTimespan> timespanSubject;
    private final EnumAdapter<TrendingTimespan> timespanAdapter;
    private final TrendingAdapter trendingAdapter;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    public TrendingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            AnovelmousApp.get(context).inject(this);
        }

        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);

        timespanSubject = PublishSubject.create();
        timespanAdapter = new TrendingTimespanAdapter(
                new ContextThemeWrapper(getContext(), R.style.Theme_Anovelmous_TrendingTimespan));

        trendingAdapter = new TrendingAdapter(this);
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

        timespanView.setAdapter(timespanAdapter);

        swipeRefreshView.setColorSchemeResources(R.color.accent);
        swipeRefreshView.setOnRefreshListener(this);

        trendingAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                animatorView.setDisplayedChildId(R.id.trending_swipe_refresh);
                swipeRefreshView.setRefreshing(false);
            }
        });

        novelsView.setLayoutManager(new LinearLayoutManager(getContext()));
        novelsView.addItemDecoration(
                new DividerItemDecoration(getContext(), VERTICAL_LIST, dividerPaddingStart, safeIsRtl()));
        novelsView.setAdapter(trendingAdapter);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        subscriptions.add(timespanSubject //
                .flatMap(trendingSearch) //
                .map(SearchResultToNovelList.instance())
                .subscribe(trendingAdapter));

        // Load the default selection.
        onRefresh();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        subscriptions.unsubscribe();
    }

    @OnItemSelected(R.id.trending_timespan) void timespanSelected(final int position) {
        if (animatorView.getDisplayedChildId() == R.id.trending_error) {
            animatorView.setDisplayedChildId(R.id.trending_loading);
        }

        // For whatever reason, the SRL's spinner does not draw itself when we call setRefreshing(true)
        // unless it is posted.
        post(new Runnable() {
            @Override public void run() {
                swipeRefreshView.setRefreshing(true);
                timespanSubject.onNext(timespanAdapter.getItem(position));
            }
        });
    }

    @Override public void onRefresh() {
        timespanSelected(timespanView.getSelectedItemPosition());
    }

    @Override public void onNovelClick(Novel novel) {
        //Intents.maybeStartActivity(getContext(), intentFactory.createUrlIntent(novel.htmlUrl));
    }

    private boolean safeIsRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }

    private final Func1<TrendingTimespan, Observable<NovelsResponse>> trendingSearch =
            new Func1<TrendingTimespan, Observable<NovelsResponse>>() {
                @Override public Observable<NovelsResponse> call(TrendingTimespan trendingTimespan) {
                    return anovelmousService.novels(Sort.UPDATED, Order.DESC)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError(trendingError)
                            .onErrorResumeNext(Observable.<NovelsResponse>empty());
                }
            };

    private final Action1<Throwable> trendingError = new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to get trending repositories");
            swipeRefreshView.setRefreshing(false);
            animatorView.setDisplayedChildId(R.id.trending_error);
        }
    };
}

package com.anovelmous.app.ui.novels;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
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
import com.anovelmous.app.data.api.NetworkService;
import com.anovelmous.app.data.api.PersistenceService;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.ui.chapters.ChapterSelectActivity;
import com.anovelmous.app.ui.misc.BetterViewAnimator;
import com.anovelmous.app.ui.misc.DividerItemDecoration;
import com.anovelmous.app.ui.misc.EnumAdapter;
import com.anovelmous.app.ui.trending.TrendingTimespan;
import com.anovelmous.app.ui.trending.TrendingTimespanAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.anovelmous.app.ui.misc.DividerItemDecoration.VERTICAL_LIST;

/**
 * Created by Greg Ziegan on 6/1/15.
 */
public class NovelSelectView extends LinearLayout
        implements SwipeRefreshLayout.OnRefreshListener, NovelSelectAdapter.NovelClickListener {
    public final static String NOVEL_ID = "com.anovelmous.app.ui.novels.NOVEL_ID";

    @InjectView(R.id.novels_select_toolbar) Toolbar toolbarView;
    @InjectView(R.id.trending_timespan) Spinner timespanView;
    @InjectView(R.id.novels_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.novels_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.novels_list) RecyclerView novelsView;
    @InjectView(R.id.novels_loading_message) TextView loadingMessageView;

    @Inject NetworkService networkService;
    @Inject PersistenceService persistenceService;
    @Inject IntentFactory intentFactory;

    private final float dividerPaddingStart;

    private final PublishSubject<TrendingTimespan> timespanSubject;
    private final EnumAdapter<TrendingTimespan> timespanAdapter;
    private final NovelSelectAdapter novelSelectAdapter;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final RestService restService;

    public NovelSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            AnovelmousApp.get(context).inject(this);
        }

        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);

        timespanSubject = PublishSubject.create();
        timespanAdapter = new TrendingTimespanAdapter(
                new ContextThemeWrapper(getContext(), R.style.Theme_Anovelmous_TrendingTimespan));

        novelSelectAdapter = new NovelSelectAdapter(this, context);
        restService = new AnovelmousService(networkService, persistenceService);
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

        novelSelectAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                animatorView.setDisplayedChildId(R.id.novels_swipe_refresh);
                swipeRefreshView.setRefreshing(false);
            }
        });

        novelsView.setLayoutManager(new LinearLayoutManager(getContext()));
        novelsView.addItemDecoration(
                new DividerItemDecoration(getContext(), VERTICAL_LIST, dividerPaddingStart, safeIsRtl()));
        novelsView.setAdapter(novelSelectAdapter);
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        subscriptions.add(restService.getAllNovels()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(trendingError)
                .onErrorResumeNext(Observable.<List<Novel>>empty())
                .subscribe(novelSelectAdapter));

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
        Intent intent = new Intent(getContext(), ChapterSelectActivity.class);
        intent.putExtra(NOVEL_ID, novel.id);
        getContext().startActivity(intent);
    }

    private boolean safeIsRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }

    private final Action1<Throwable> trendingError = new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to get trending novels");
            swipeRefreshView.setRefreshing(false);
            animatorView.setDisplayedChildId(R.id.trending_error);
        }
    };
}

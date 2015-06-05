package com.anovelmous.app.ui.chapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.R;
import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.Order;
import com.anovelmous.app.data.api.Sort;
import com.anovelmous.app.data.api.model.Chapter;
import com.anovelmous.app.data.api.model.ChaptersResponse;
import com.anovelmous.app.data.api.transforms.SearchResultToChapterList;
import com.anovelmous.app.ui.misc.BetterViewAnimator;
import com.anovelmous.app.ui.misc.DividerItemDecoration;
import com.anovelmous.app.ui.misc.GoUpClickListener;
import com.anovelmous.app.ui.novels.NovelSelectView;
import com.anovelmous.app.ui.reading.ReadingActivity;
import com.anovelmous.app.ui.reading.ReadingView;
import com.anovelmous.app.util.Intents;

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
        implements SwipeRefreshLayout.OnRefreshListener, ChapterSelectAdapter.ChapterClickListener {
    public final static String CHAPTER_ID = "com.anovelmous.app.ui.chapters.CHAPTER_ID";

    @InjectView(R.id.chapters_select_toolbar) Toolbar toolbarView;
    @InjectView(R.id.chapters_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.chapters_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.chapter_list) RecyclerView chaptersView;
    @InjectView(R.id.chapters_loading_message) TextView loadingMessageView;

    @Inject AnovelmousService anovelmousService;
    private final GoUpClickListener goUpClickListener;

    private final float dividerPaddingStart;
    private final PublishSubject<Long> novelIdSubject;
    private final ChapterSelectAdapter chapterSelectAdapter;
    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private final long novelId;

    public ChapterSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            AnovelmousApp.get(context).inject(this);
            Intent intent = ((Activity) context).getIntent();
            novelId = intent.getLongExtra(NovelSelectView.NOVEL_ID, 1);
        } else
            novelId = 1;

        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);
        novelIdSubject = PublishSubject.create();
        chapterSelectAdapter = new ChapterSelectAdapter(this);
        goUpClickListener = new GoUpClickListener(context);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        AnimationDrawable ellipsis =
                (AnimationDrawable) getResources().getDrawable(R.drawable.dancing_ellipsis);
        loadingMessageView.setCompoundDrawablesWithIntrinsicBounds(null, null, ellipsis, null);
        ellipsis.start();

        swipeRefreshView.setColorSchemeResources(R.color.accent);
        swipeRefreshView.setOnRefreshListener(this);

        toolbarView.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbarView.setNavigationOnClickListener(goUpClickListener);

        chapterSelectAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                animatorView.setDisplayedChildId(R.id.chapters_swipe_refresh);
                swipeRefreshView.setRefreshing(false);
            }
        });

        chaptersView.setLayoutManager(new LinearLayoutManager(getContext()));
        chaptersView.addItemDecoration(
                new DividerItemDecoration(getContext(), VERTICAL_LIST, dividerPaddingStart, safeIsRtl()));
        chaptersView.setAdapter(chapterSelectAdapter);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        subscriptions.add(novelIdSubject
                .flatMap(chaptersRequest)
                .map(SearchResultToChapterList.instance())
                .subscribe(chapterSelectAdapter));

        onRefresh();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        subscriptions.unsubscribe();
    }

    private boolean safeIsRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
        return getLayoutDirection() == LAYOUT_DIRECTION_RTL;
    }

    @Override
    public void onChapterClick(Chapter chapter) {
        Intent intent = new Intent(getContext(), ReadingActivity.class);
        intent.putExtra(CHAPTER_ID, chapter.id);
        getContext().startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (animatorView.getDisplayedChildId() == R.id.trending_error) {
            animatorView.setDisplayedChildId(R.id.trending_loading);
        }

        post(new Runnable() {
            @Override public void run() {
                swipeRefreshView.setRefreshing(true);
                novelIdSubject.onNext(novelId);
            }
        });
    }

    private final Func1<Long, Observable<ChaptersResponse>> chaptersRequest =
            new Func1<Long, Observable<ChaptersResponse>>() {
                @Override
                public Observable<ChaptersResponse> call(Long novelId) {
                    return anovelmousService.chapters(novelId, Sort.UPDATED, Order.DESC)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError(chapterLoadError)
                            .onErrorResumeNext(Observable.<ChaptersResponse>empty());
                }
            };

    private final Action1<Throwable> chapterLoadError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to get the novel's chapters");
            swipeRefreshView.setRefreshing(false);
            animatorView.setDisplayedChildId(R.id.trending_error);
        }
    };
}

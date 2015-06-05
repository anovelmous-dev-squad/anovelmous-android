package com.anovelmous.app.ui.reading;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.R;
import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.Order;
import com.anovelmous.app.data.api.Sort;
import com.anovelmous.app.data.api.model.ChapterTextResponse;
import com.anovelmous.app.data.api.model.FormattedNovelToken;
import com.anovelmous.app.data.api.transforms.SearchResultToFormattedNovelTokenList;
import com.anovelmous.app.ui.chapters.ChapterSelectView;
import com.anovelmous.app.ui.misc.BetterViewAnimator;
import com.anovelmous.app.ui.misc.DividerItemDecoration;
import com.anovelmous.app.ui.misc.GoUpClickListener;

import java.text.Normalizer;
import java.util.Collections;
import java.util.List;

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
 * Created by Greg Ziegan on 6/4/15.
 */
public class ReadingView extends LinearLayout
    implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.reading_toolbar) Toolbar toolbarView;
    @InjectView(R.id.reading_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.reading_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.reading_loading_message) TextView loadingMessageView;
    @InjectView(R.id.reading_text_content) TextView textContent;

    @Inject AnovelmousService anovelmousService;

    private final long chapterId;
    private final float dividerPaddingStart;
    private final PublishSubject<Long> chapterIdSubject;
    private final ReadingAdapter readingAdapter;
    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final GoUpClickListener goUpClickListener;
    private List<FormattedNovelToken> chapterText = Collections.emptyList();

    public ReadingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode()) {
            AnovelmousApp.get(context).inject(this);
            Intent intent = ((Activity) context).getIntent();
            chapterId = intent.getLongExtra(ChapterSelectView.CHAPTER_ID, 1);
        } else
            chapterId = 1;

        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);

        chapterIdSubject = PublishSubject.create();
        readingAdapter = new ReadingAdapter(context, R.id.reading_text_content, chapterText);
        goUpClickListener = new GoUpClickListener(context);
    }

    @Override
    protected void onFinishInflate() {
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

        readingAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                animatorView.setDisplayedChildId(R.id.reading_swipe_refresh);
                swipeRefreshView.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        subscriptions.add(chapterIdSubject
                .flatMap(chapterTextRequest)
                .map(SearchResultToFormattedNovelTokenList.instance())
                .subscribe(readingAdapter));

        onRefresh();
    }

    @Override
    public void onRefresh() {
        post(new Runnable() {
            @Override public void run() {
                swipeRefreshView.setRefreshing(true);
                chapterIdSubject.onNext(chapterId);
            }
        });
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

    private final Func1<Long, Observable<ChapterTextResponse>> chapterTextRequest =
            new Func1<Long, Observable<ChapterTextResponse>>() {
                @Override
                public Observable<ChapterTextResponse> call(Long aLong) {
                    return anovelmousService.chapterText(chapterId, Sort.UPDATED, Order.DESC)
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnError(chapterTextLoadError)
                            .onErrorResumeNext(Observable.<ChapterTextResponse>empty());
                }
            };

    private final Action1<Throwable> chapterTextLoadError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to get the Chapter's items.");
            swipeRefreshView.setRefreshing(false);
            animatorView.setDisplayedChildId(R.id.reading_error);
        }
    };

}

package com.anovelmous.app.ui.chapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anovelmous.app.Injector;
import com.anovelmous.app.R;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.ui.BaseFragment;
import com.anovelmous.app.ui.misc.BetterViewAnimator;
import com.anovelmous.app.ui.misc.DividerItemDecoration;
import com.anovelmous.app.ui.novels.NovelSelectFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.anovelmous.app.ui.misc.DividerItemDecoration.VERTICAL_LIST;

public class ChapterSelectFragment extends BaseFragment implements Injector,
        SwipeRefreshLayout.OnRefreshListener, ChapterSelectAdapter.ChapterClickListener {
    public final static String CHAPTER_ID = "com.anovelmous.app.ui.chapters.CHAPTER_ID";

    @Inject RestService restService;

    private View inflatedView;
    private String novelId;

    @InjectView(R.id.chapters_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.chapters_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.chapter_list) RecyclerView chaptersView;
    @InjectView(R.id.chapters_loading_message) TextView loadingMessageView;

    private float dividerPaddingStart;
    private PublishSubject<String> novelIdSubject;
    private ChapterSelectAdapter chapterSelectAdapter;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public static ChapterSelectFragment newInstance(String novelId) {
        ChapterSelectFragment fragment = new ChapterSelectFragment();
        Bundle args = new Bundle();
        args.putString(NovelSelectFragment.NOVEL_ID, novelId);
        fragment.setArguments(args);
        return fragment;
    }

    public ChapterSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            novelId = getArguments().getString(NovelSelectFragment.NOVEL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedView = inflater.inflate(R.layout.fragment_chapter_select, container, false);
        // Inflate the layout for this fragment
        ButterKnife.inject(this, inflatedView);

        AnimationDrawable ellipsis =
                (AnimationDrawable) getResources().getDrawable(R.drawable.dancing_ellipsis);
        loadingMessageView.setCompoundDrawablesWithIntrinsicBounds(null, null, ellipsis, null);
        ellipsis.start();

        swipeRefreshView.setColorSchemeResources(R.color.accent);
        swipeRefreshView.setOnRefreshListener(this);

        chapterSelectAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                animatorView.setDisplayedChildId(R.id.chapters_swipe_refresh);
                swipeRefreshView.setRefreshing(false);
            }
        });

        chaptersView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chaptersView.addItemDecoration(
                new DividerItemDecoration(getActivity(), VERTICAL_LIST, dividerPaddingStart, safeIsRtl()));
        chaptersView.setAdapter(chapterSelectAdapter);
        onRefresh();
        return inflatedView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);
        novelIdSubject = PublishSubject.create();
        chapterSelectAdapter = new ChapterSelectAdapter(this, getActivity());
        subscriptions.add(novelIdSubject
                .flatMap(chaptersRequest)
                .subscribe(chapterSelectAdapter));
    }

    @Override
    public void onRefresh() {
        if (animatorView.getDisplayedChildId() == R.id.trending_error) {
            animatorView.setDisplayedChildId(R.id.trending_loading);
        }

        inflatedView.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshView.setRefreshing(true);
                novelIdSubject.onNext(novelId);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subscriptions.unsubscribe();
    }

    @Override
    public void onChapterClick(Chapter chapter) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private boolean safeIsRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
        return inflatedView.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    private final Func1<String, Observable<List<Chapter>>> chaptersRequest =
            new Func1<String, Observable<List<Chapter>>>() {
                @Override
                public Observable<List<Chapter>> call(String novelId) {
                    return restService.getNovelChapters(novelId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .doOnError(chapterLoadError)
                            .onErrorResumeNext(Observable.<List<Chapter>>empty());
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

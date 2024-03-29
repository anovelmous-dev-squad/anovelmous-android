package com.anovelmous.app.ui.novels;

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

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.ui.BaseFragment;
import com.anovelmous.app.ui.chapters.ChapterSelectFragment;
import com.anovelmous.app.ui.misc.BetterViewAnimator;
import com.anovelmous.app.ui.misc.DividerItemDecoration;

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

public class NovelSelectFragment extends BaseFragment
        implements SwipeRefreshLayout.OnRefreshListener, NovelSelectAdapter.NovelClickListener {
    public final static String NOVEL_ID = "com.anovelmous.app.ui.novels.NOVEL_ID";

    @Inject RestService restService;

    @InjectView(R.id.novels_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.novels_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.novels_list) RecyclerView novelsView;
    @InjectView(R.id.novels_loading_message) TextView loadingMessageView;

    private float dividerPaddingStart;

    private PublishSubject<RestVerb> novelsSubject;
    private NovelSelectAdapter novelSelectAdapter;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    private View inflatedView;

    public static NovelSelectFragment newInstance() {
        NovelSelectFragment fragment = new NovelSelectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public NovelSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_novel_select, container, false);
        ButterKnife.inject(this, inflatedView);

        AnimationDrawable ellipsis =
                (AnimationDrawable) getResources().getDrawable(R.drawable.dancing_ellipsis);
        loadingMessageView.setCompoundDrawablesWithIntrinsicBounds(null, null, ellipsis, null);
        ellipsis.start();

        swipeRefreshView.setColorSchemeResources(R.color.accent);
        swipeRefreshView.setOnRefreshListener(this);

        novelSelectAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                animatorView.setDisplayedChildId(R.id.novels_swipe_refresh);
                swipeRefreshView.setRefreshing(false);
            }
        });

        novelsView.setLayoutManager(new LinearLayoutManager(getActivity()));
        novelsView.addItemDecoration(
                new DividerItemDecoration(getActivity(), VERTICAL_LIST, dividerPaddingStart, safeIsRtl()));
        novelsView.setAdapter(novelSelectAdapter);

        onRefresh();
        return inflatedView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        novelsSubject = PublishSubject.create();

        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);

        novelSelectAdapter = new NovelSelectAdapter(this, getActivity());
        subscriptions.add(novelsSubject
                .flatMap(allNovels)
                .subscribe(novelSelectAdapter));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subscriptions.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
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
                novelsSubject.onNext(RestVerb.GET);
            }
        });
    }

    @Override
    public void onNovelClick(Novel novel) {
        ChapterSelectFragment chapterSelectFragment = ChapterSelectFragment.newInstance(String.valueOf(novel.id));
        getFragmentManager().beginTransaction()
                .replace(R.id.scroll_container, chapterSelectFragment).addToBackStack(null).commit();
    }

    private final Func1<RestVerb, Observable<List<Novel>>> allNovels =
            new Func1<RestVerb, Observable<List<Novel>>>() {
                @Override public Observable<List<Novel>> call(RestVerb restVerb) {
                    return restService.getAllNovels()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .doOnError(novelsLoadError)
                            .onErrorResumeNext(Observable.<List<Novel>>empty());
                }
            };

    private final Action1<Throwable> novelsLoadError = new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to get trending novels");
            swipeRefreshView.setRefreshing(false);
            animatorView.setDisplayedChildId(R.id.trending_error);
        }
    };

    private boolean safeIsRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
        return inflatedView.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

}

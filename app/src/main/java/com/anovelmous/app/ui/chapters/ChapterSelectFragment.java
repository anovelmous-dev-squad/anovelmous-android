package com.anovelmous.app.ui.chapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anovelmous.app.InjectingFragmentModule;
import com.anovelmous.app.Injector;
import com.anovelmous.app.R;
import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.NetworkService;
import com.anovelmous.app.data.api.PersistenceService;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.ui.misc.BetterViewAnimator;
import com.anovelmous.app.ui.misc.DividerItemDecoration;
import com.anovelmous.app.ui.misc.GoUpClickListener;
import com.anovelmous.app.ui.novels.NovelSelectFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static com.anovelmous.app.ui.misc.DividerItemDecoration.VERTICAL_LIST;
import static com.anovelmous.app.util.Preconditions.checkState;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChapterSelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChapterSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChapterSelectFragment extends Fragment implements Injector,
        SwipeRefreshLayout.OnRefreshListener, ChapterSelectAdapter.ChapterClickListener {
    public final static String CHAPTER_ID = "com.anovelmous.app.ui.chapters.CHAPTER_ID";

    private View inflatedView;
    private long novelId;
    private ObjectGraph mObjectGraph;
    private boolean mFirstAttach = true;

    @InjectView(R.id.chapters_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.chapters_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.chapter_list) RecyclerView chaptersView;
    @InjectView(R.id.chapters_loading_message) TextView loadingMessageView;

    @Inject NetworkService networkService;
    @Inject PersistenceService persistenceService;

    private float dividerPaddingStart;
    private PublishSubject<Long> novelIdSubject;
    private ChapterSelectAdapter chapterSelectAdapter;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private RestService restService;

    private OnFragmentInteractionListener mListener;

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
            novelId = getArguments().getLong(NovelSelectFragment.NOVEL_ID);
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
        return inflatedView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        ObjectGraph appGraph = ((Injector) activity).getObjectGraph();
        List<Object> fragmentModules = getModules();
        mObjectGraph = appGraph.plus(fragmentModules.toArray());

        if (mFirstAttach) {
            inject(this);
            mFirstAttach = false;
        }

        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);
        novelIdSubject = PublishSubject.create();
        chapterSelectAdapter = new ChapterSelectAdapter(this, getActivity());
        restService = new AnovelmousService(networkService, persistenceService);
        subscriptions.add(novelIdSubject
                .flatMap(chaptersRequest)
                .subscribe(chapterSelectAdapter));

        onRefresh();
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
        mListener = null;
        subscriptions.unsubscribe();
    }

    @Override
    public void onChapterClick(Chapter chapter) {

    }

    @Override
    public void onDestroy() {
        mObjectGraph = null;
        super.onDestroy();
    }

    private boolean safeIsRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isRtl();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1) private boolean isRtl() {
        return inflatedView.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private final Func1<Long, Observable<List<Chapter>>> chaptersRequest =
            new Func1<Long, Observable<List<Chapter>>>() {
                @Override
                public Observable<List<Chapter>> call(Long novelId) {
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

    @Override
    public ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    @Override
    public void inject(Object target) {
        checkState(mObjectGraph != null, "object graph must be assigned prior to calling inject");
        mObjectGraph.inject(target);
    }

    protected List<Object> getModules() {
        List<Object> result = new ArrayList<>();
        result.add(new InjectingFragmentModule(this, this));
        return result;
    }
}

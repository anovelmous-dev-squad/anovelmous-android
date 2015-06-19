package com.anovelmous.app.ui.novels;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.NetworkService;
import com.anovelmous.app.data.api.PersistenceService;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Novel;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NovelSelectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NovelSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NovelSelectFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, NovelSelectAdapter.NovelClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public final static String NOVEL_ID = "com.anovelmous.app.ui.novels.NOVEL_ID";

    @InjectView(R.id.novels_animator) BetterViewAnimator animatorView;
    @InjectView(R.id.novels_swipe_refresh) SwipeRefreshLayout swipeRefreshView;
    @InjectView(R.id.novels_list) RecyclerView novelsView;
    @InjectView(R.id.novels_loading_message) TextView loadingMessageView;

    @Inject NetworkService networkService;
    @Inject PersistenceService persistenceService;

    private float dividerPaddingStart;

    private PublishSubject<RestVerb> novelsSubject;
    private NovelSelectAdapter novelSelectAdapter;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private RestService restService;


    private View inflatedView;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NovelSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NovelSelectFragment newInstance(String param1, String param2) {
        NovelSelectFragment fragment = new NovelSelectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NovelSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_novel_select, container, false);
        ButterKnife.inject(inflatedView);

        dividerPaddingStart =
                getResources().getDimensionPixelSize(R.dimen.trending_divider_padding_start);

        novelsSubject = PublishSubject.create();

        novelSelectAdapter = new NovelSelectAdapter(this, getActivity());
        restService = new AnovelmousService(networkService, persistenceService);

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

        subscriptions.add(novelsSubject
                .flatMap(allNovels)
                .subscribe(novelSelectAdapter));

        onRefresh();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        subscriptions.unsubscribe();
    }

    @Override public void onRefresh() {
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
        void onFragmentInteraction(Uri uri);
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
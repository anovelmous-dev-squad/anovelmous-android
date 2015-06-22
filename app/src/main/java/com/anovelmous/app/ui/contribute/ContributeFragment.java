package com.anovelmous.app.ui.contribute;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.ui.BaseFragment;

import java.util.List;

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

public class ContributeFragment extends BaseFragment {

    @InjectView(R.id.contribute_auto_complete_view) MultiAutoCompleteTextView autoCompleteTextView;

    private AutoCompleteAdapter autoCompleteAdapter;
    private PublishSubject<RestVerb> tokenFilterSubject;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    public static ContributeFragment newInstance() {
        ContributeFragment fragment = new ContributeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ContributeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contribute, container, false);
        ButterKnife.inject(this, view);

        autoCompleteTextView.setAdapter(autoCompleteAdapter);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tokenFilterSubject = PublishSubject.create();
        autoCompleteAdapter = new AutoCompleteAdapter(getActivity(), R.layout.fragment_contribute);
        subscriptions.add(tokenFilterSubject
                .flatMap(filteredTokens)
                .subscribe(autoCompleteAdapter));
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

    private final Func1<RestVerb, Observable<List<String>>> filteredTokens = new Func1<RestVerb, Observable<List<String>>>() {
        @Override
        public Observable<List<String>> call(RestVerb restVerb) {
            return restService.getGrammarFilteredStrings()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError(tokensLoadError)
                    .onErrorResumeNext(Observable.<List<String>>empty());
        }
    };

    private final Action1<Throwable> tokensLoadError = new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to get filtered tokens");
        }
    };
}

package com.anovelmous.app.ui.contribute;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.Vote;
import com.anovelmous.app.ui.BaseActivity;
import com.anovelmous.app.ui.BaseFragment;
import com.anovelmous.app.ui.LoggedInActivity;
import com.anovelmous.app.ui.reading.ReadingFragment;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class ContributeFragment extends BaseFragment {

    @InjectView(R.id.contribute_auto_complete_view) AutoCompleteTextView autoCompleteTextView;

    private boolean isRefreshing;
    private AutoCompleteAdapter autoCompleteAdapter;
    private PublishSubject<RestVerb> tokenFilterSubject;
    private PublishSubject<Long> currentChapterSubject;
    private PublishSubject<String> voteSubject;
    private CompositeSubscription subscriptions = new CompositeSubscription();
    private long currentChapterId;
    private Chapter currentChapter;
    private int pollingTime = 10;

    public static ContributeFragment newInstance(long currentChapterId) {
        ContributeFragment fragment = new ContributeFragment();
        Bundle args = new Bundle();
        args.putLong(ReadingFragment.CUR_CHAPTER_ID, currentChapterId);
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
            currentChapterId = getArguments().getLong(ReadingFragment.CUR_CHAPTER_ID, 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contribute, container, false);
        ButterKnife.inject(this, view);

        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch(result) {
                    case EditorInfo.IME_ACTION_DONE:
                        voteSubject.onNext((String) v.getText());
                        break;
                    case EditorInfo.IME_ACTION_SEARCH:
                        break;
                }
                return true;
            }
        });

        isRefreshing = true;

        Observable.interval(pollingTime, TimeUnit.SECONDS, Schedulers.io())
                .takeWhile(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return isRefreshing;
                    }
                })
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long tick) {
                        Timber.d("Polling for new tokens");
                        tokenFilterSubject.onNext(RestVerb.GET);
                    }
                });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tokenFilterSubject = PublishSubject.create();
        autoCompleteAdapter = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_list_item_1);
        subscriptions.add(tokenFilterSubject
                .flatMap(filteredTokens)
                .subscribe(autoCompleteAdapter));

        currentChapterSubject = PublishSubject.create();
        subscriptions.add(currentChapterSubject
                .flatMap(chapterRefresh)
                .subscribe(new Action1<Chapter>() {
                    @Override
                    public void call(Chapter chapter) {
                        Timber.d("Current 'Contribute' Chapter set to: " + chapter);
                        currentChapter = chapter;
                        pollingTime = chapter.votingDuration;
                    }
                }));

        currentChapterSubject.onNext(currentChapterId);

        voteSubject = PublishSubject.create();
        subscriptions.add(voteSubject
            .flatMap(castVote)
            .subscribe(new Action1<Vote>() {
                @Override
                public void call(Vote vote) {
                    Timber.d("Successfully casted a vote!");
                }
            }));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subscriptions.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isRefreshing = false;
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

    private final Func1<Long, Observable<Chapter>> chapterRefresh = new Func1<Long, Observable<Chapter>>() {
        @Override
        public Observable<Chapter> call(Long chapterId) {
            return restService.getChapter(currentChapterId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError(chapterLoadError)
                    .retry(3);
        }
    };

    private final Action1<Throwable> chapterLoadError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to refresh chapter");
        }
    };

    private final Func1<String, Observable<Vote>> castVote = new Func1<String, Observable<Vote>>() {
        @Override
        public Observable<Vote> call(String tokenContent) {
            return Observable.just(tokenContent)
                    .flatMap(new Func1<String, Observable<Token>>() {
                        @Override
                        public Observable<Token> call(String tokenContent) {
                            return restService.getTokenFromContent(tokenContent);
                        }
                    })
                    .map(new Func1<Token, Vote>() {
                        @Override
                        public Vote call(Token token) {
                            return new Vote.Builder()
                                    .restVerb(RestVerb.POST)
                                    .chapter(currentChapter.url)
                                    .ordinal(1) // TODO: Lookup most recent noveltoken ordinal
                                    .token(token.url)
                                    .user(null)// ((LoggedInActivity) getActivity()).getCurrentUser().url)
                                    .build();
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError(voteCastError);
        }
    };

    private final Action1<Throwable> voteCastError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            Timber.e(throwable, "Failed to cast vote");
        }
    };
}

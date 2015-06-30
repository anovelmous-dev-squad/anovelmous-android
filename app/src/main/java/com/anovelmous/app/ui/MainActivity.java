package com.anovelmous.app.ui;


import android.content.Intent;
import android.os.Bundle;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Contributor;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public final class MainActivity extends BaseActivity {

    @Inject RestService restService;
    private PublishSubject<RestVerb> getUserSubject;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        final Intent intent;
        if (accessToken == null) {  // Check if logged in through Facebook
            intent = new Intent(this, LoggedOutActivity.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.login_loading);
            getUserSubject = PublishSubject.create();
            intent = new Intent(this, LoggedInActivity.class);
            subscriptions.add(getUserSubject
                    .flatMap(new Func1<RestVerb, Observable<Contributor>>() {
                        @Override
                        public Observable<Contributor> call(RestVerb restVerb) {
                            return restService.getContributor(accessToken)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .doOnError(new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            Timber.e("Failed to get Contributor from FB access token.");
                                        }
                                    })
                                    .onErrorResumeNext(Observable.<Contributor>empty());
                        }
                    })
                    .subscribe(new Action1<Contributor>() {
                        @Override
                        public void call(Contributor contributor) {
                            Timber.d("Launching LoggedInActivity");
                            intent.putExtra(LoggedOutActivity.USER_LOGIN_ID, contributor.id);
                            startActivity(intent);
                            finish();
                        }
                    }));

        }
        // TODO: check if logged in w/o facebook
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getUserSubject.onNext(RestVerb.GET);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}

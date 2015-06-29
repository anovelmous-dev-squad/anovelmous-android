package com.anovelmous.app.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.NetworkService;
import com.anovelmous.app.data.api.PersistenceService;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.User;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public final class MainActivity extends BaseActivity {

    @Inject NetworkService networkService;
    @Inject PersistenceService persistenceService;
    private RestService restService; // TODO: make the rest service an injectable singleton
    private PublishSubject<RestVerb> getUserSubject;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        restService = new AnovelmousService(networkService, persistenceService);

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
                    .flatMap(new Func1<RestVerb, Observable<User>>() {
                        @Override
                        public Observable<User> call(RestVerb restVerb) {
                            return restService.getUser(accessToken)
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .doOnError(new Action1<Throwable>() {
                                        @Override
                                        public void call(Throwable throwable) {
                                            Timber.e("Failed to get User from FB access token.");
                                        }
                                    })
                                    .onErrorResumeNext(Observable.<User>empty());
                        }
                    })
                    .subscribe(new Action1<User>() {
                        @Override
                        public void call(User user) {
                            Timber.d("Launching LoggedInActivity");
                            intent.putExtra(LoggedOutActivity.USER_LOGIN_ID, user.id);
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

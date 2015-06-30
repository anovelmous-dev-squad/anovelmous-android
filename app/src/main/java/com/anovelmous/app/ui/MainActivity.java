package com.anovelmous.app.ui;


import android.content.Intent;
import android.os.Bundle;

import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.resource.Contributor;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public final class MainActivity extends BaseActivity {

    @Inject RestService restService;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null) {  // Check if logged in through Facebook
            final Intent logoutIntent = new Intent(this, LoggedOutActivity.class);
            startActivity(logoutIntent);
        } else {
            final Intent loginIntent = new Intent(this, LoggedInActivity.class);
            subscriptions.add(restService.getContributor(accessToken)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .doOnError(new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Timber.e("Failed to get Contributor from FB access token.");
                        }
                    })
                    .onErrorResumeNext(Observable.<Contributor>empty())
                    .subscribe(new Action1<Contributor>() {
                        @Override
                        public void call(Contributor contributor) {
                            Timber.d("Launching LoggedInActivity");
                            loginIntent.putExtra(LoggedOutActivity.USER_LOGIN_ID, contributor.id);
                            startActivity(loginIntent);
                        }
                    }));

        }
        // TODO: check if logged in w/o facebook
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}

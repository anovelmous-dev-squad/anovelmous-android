package com.anovelmous.app.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.anovelmous.app.AnovelmousModule;
import com.anovelmous.app.R;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.resource.Contributor;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public final class MainActivity extends BaseActivity {
    private static final long SPLASH_TIME_OUT = 1000;
    @Inject @AnovelmousModule.Application Context appContext;
    @Inject RestService restService;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(appContext);
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                updateWithToken(currentAccessToken);
            }
        };
        setContentView(R.layout.login_loading);
        updateWithToken(AccessToken.getCurrentAccessToken());
        // TODO: check if logged in w/o facebook
    }

    private void updateWithToken(final AccessToken currentAccessToken) {
        final boolean isFbLoggedIn = currentAccessToken != null;
        if (isFbLoggedIn) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    launchLoggedInActivity(currentAccessToken);
                }
            }, SPLASH_TIME_OUT);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Timber.i("launching LoggedOut Activity");
                    final Intent logoutIntent = new Intent(MainActivity.this, LoggedOutActivity.class);
                    startActivity(logoutIntent);
                }
            }, SPLASH_TIME_OUT);
        }
    }

    private void launchLoggedInActivity(AccessToken accessToken) {
        final Intent loginIntent = new Intent(this, LoggedInActivity.class);
        restService.getContributorByFbToken(accessToken.getToken())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Contributor>() {
                    @Override
                    public void call(Contributor contributor) {
                        Timber.i("Launching LoggedInActivity");
                        loginIntent.putExtra(LoggedOutActivity.USER_LOGIN_ID, contributor.id);
                        startActivity(loginIntent);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}

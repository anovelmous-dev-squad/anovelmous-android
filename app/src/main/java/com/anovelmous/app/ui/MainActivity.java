package com.anovelmous.app.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.anovelmous.app.AnovelmousModule;
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

    @Inject @AnovelmousModule.Application Context appContext;
    @Inject RestService restService;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private AccessToken accessToken;

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
                accessToken = currentAccessToken;
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null) {  // Check if logged in through Facebook
            final Intent logoutIntent = new Intent(this, LoggedOutActivity.class);
            startActivity(logoutIntent);
        } else {
            final Intent loginIntent = new Intent(this, LoggedInActivity.class);
            restService.getContributor(accessToken.getToken())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Action1<Contributor>() {
                        @Override
                        public void call(Contributor contributor) {
                            Timber.d("Launching LoggedInActivity");
                            loginIntent.putExtra(LoggedOutActivity.USER_LOGIN_ID, contributor.id);
                            startActivity(loginIntent);
                        }
                    });
        }
        // TODO: check if logged in w/o facebook
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

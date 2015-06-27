package com.anovelmous.app.ui;

import android.os.Bundle;

import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.NetworkService;
import com.anovelmous.app.data.api.PersistenceService;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.resource.User;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Greg Ziegan on 6/27/15.
 */
public final class LoggedInActivity extends MainActivity {

    private static final String TEMP_AUTH_TOKEN = "temp_auth_token";

    @Inject NetworkService networkService;
    @Inject PersistenceService persistenceService;
    private User currentUser;
    private RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restService = new AnovelmousService(networkService, persistenceService);
        restService.getMyUser(TEMP_AUTH_TOKEN)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        currentUser = user;
                    }
                });
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

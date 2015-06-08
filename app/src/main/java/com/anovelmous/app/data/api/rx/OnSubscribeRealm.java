package com.anovelmous.app.data.api.rx;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.exceptions.RealmException;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/8/15.
 * Adapted from the code featured in the following blog post:
 *      https://realm.io/news/using-realm-with-rxjava/
 *
 */
public abstract class OnSubscribeRealm<T extends RealmObject> implements Observable.OnSubscribe<T> {
    private Context context;
    private String dbFileName;

    public OnSubscribeRealm(Context context) {
        this.context = context;
        dbFileName = null;
    }

    public OnSubscribeRealm(Context context, String dbFileName) {
        this.context = context;
        this.dbFileName = dbFileName;
    }

    @Override
    public void call(final Subscriber<? super T> subscriber) {
        final Realm realm = dbFileName != null ? Realm.getInstance(context, dbFileName) : Realm.getInstance(context);
        subscriber.add(Subscriptions.create(new Action0() {
            @Override
            public void call() {
                try {
                    realm.close();
                } catch (RealmException ex) {
                    subscriber.onError(ex);
                }
            }
        }));

        T object;
        realm.beginTransaction();
        try {
            object = get(realm);
            realm.commitTransaction();
        } catch (RuntimeException e) {
            realm.cancelTransaction();
            subscriber.onError(new RealmException("Error occurred during transaction.", e));
            return;
        } catch (Error e) {
            realm.cancelTransaction();
            subscriber.onError(e);
            return;
        }

        if (object != null) {
            subscriber.onNext(object);
        }
        subscriber.onCompleted();
    }

    public abstract T get(Realm realm);
}

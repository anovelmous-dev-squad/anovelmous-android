package com.anovelmous.app.data.api.rx;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmObject;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/8/15.
 * Adapted from the code featured in the following blog post:
 *      https://realm.io/news/using-realm-with-rxjava/
 */
public final class RealmObservable {
    private RealmObservable() {
    }

    public static <T extends RealmObject> Observable<T> object(Context context,
                                                               final Func1<Realm, T> function) {
        return Observable.create(new OnSubscribeRealm<T>(context) {
            @Override
            public T get(Realm realm) {
                return function.call(realm);
            }
        });
    }

    public static <T extends RealmObject> Observable<T> object(Context context,
                                                               String dbFileName,
                                                               final Func1<Realm, T> function) {
        return Observable.create(new OnSubscribeRealm<T>(context, dbFileName) {
            @Override
            public T get(Realm realm) {
                return function.call(realm);
            }
        });
    }
}

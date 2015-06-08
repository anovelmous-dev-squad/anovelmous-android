package com.anovelmous.app.data.api;

import android.content.Context;

import com.anovelmous.app.data.api.model.RealmNovel;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.rx.RealmObservable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/8/15.
 */
public class RealmDataService implements DataService {
    private final Context context;

    public RealmDataService(Context context) {
        this.context = context;
    }

    @Override
    public Observable<List<Novel>> novels() {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmNovel>>() {
            @Override
            public RealmResults<RealmNovel> call(Realm realm) {
                return realm.where(RealmNovel.class).findAll();
            }
        }).map(new Func1<RealmResults<RealmNovel>, List<Novel>>() {
            @Override
            public List<Novel> call(RealmResults<RealmNovel> realmNovels) {
                final List<Novel> novels = new ArrayList<>(realmNovels.size());
                for (RealmNovel realmNovel : realmNovels)
                    novels.add(novelFromRealm(realmNovel));
                return novels;
            }
        });
    }

    private static Novel novelFromRealm(RealmNovel realmNovel) {
        return new Novel.Builder()
                .id(realmNovel.getId())
                .url(realmNovel.getUrl())
                .title(realmNovel.getTitle())
                .isCompleted(realmNovel.isCompleted())
                .votingDuration(realmNovel.getVotingDuration())
                .createdAt(new DateTime (realmNovel.getCreatedAt()))
                .build();
    }

}

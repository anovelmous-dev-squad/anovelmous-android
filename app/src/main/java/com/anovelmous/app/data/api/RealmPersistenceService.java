package com.anovelmous.app.data.api;

import android.content.Context;

import com.anovelmous.app.data.api.model.RealmChapter;
import com.anovelmous.app.data.api.model.RealmNovel;
import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.rx.RealmObservable;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/8/15.
 */
public class RealmPersistenceService implements PersistenceService {
    private final Context context;

    public RealmPersistenceService(Context context) {
        this.context = context;
    }

    private Observable<RealmResults<RealmNovel>> getRealmNovels() {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmNovel>>() {
            @Override
            public RealmResults<RealmNovel> call(Realm realm) {
                return realm.where(RealmNovel.class).findAll();
            }
        });
    }

    @Override
    public Observable<List<Novel>> novels() {
        return getRealmNovels().map(new Func1<RealmResults<RealmNovel>, List<Novel>>() {
            @Override
            public List<Novel> call(RealmResults<RealmNovel> realmNovels) {
                final List<Novel> novels = new ArrayList<>(realmNovels.size());
                for (RealmNovel realmNovel : realmNovels)
                    novels.add(novelFromRealm(realmNovel));
                return novels;
            }
        });
    }

    @Override
    public Observable<ResourceCount> novelsCount() {
        return getRealmNovels().map(new Func1<RealmResults<RealmNovel>, ResourceCount>() {
            @Override
            public ResourceCount call(RealmResults<RealmNovel> realmNovels) {
                return new ResourceCount.Builder().
                        count(realmNovels.size())
                        .build();
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

    @Override
    public Observable<Novel> saveNovel(final Novel novel) {
        return RealmObservable.object(context, new Func1<Realm, RealmNovel>() {
            @Override
            public RealmNovel call(Realm realm) {
                RealmNovel realmNovel = new RealmNovel(novel);
                return realm.copyToRealmOrUpdate(realmNovel);
            }
        }).map(new Func1<RealmNovel, Novel>() {
            @Override
            public Novel call(RealmNovel realmNovel) {
                return novelFromRealm(realmNovel);
            }
        });
    }

    private Observable<RealmResults<RealmChapter>> getRealmNovelChapters() {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmChapter>>() {
            @Override
            public RealmResults<RealmChapter> call(Realm realm) {
                return realm.where(RealmChapter.class).findAll();
            }
        });
    }

    private static Chapter chapterFromRealm(RealmChapter realmChapter) {
        return new Chapter.Builder()
                .id(realmChapter.getId())
                .url(realmChapter.getUrl())
                .title(realmChapter.getTitle())
                .isCompleted(realmChapter.isCompleted())
                .votingDuration(realmChapter.getVotingDuration())
                .novel(realmChapter.getNovel().getUrl())
                .createdAt(new DateTime(realmChapter.getCreatedAt()))
                .build();
    }

    @Override
    public Observable<List<Chapter>> chapters(long novelId) {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmChapter>>() {
            @Override
            public RealmResults<RealmChapter> call(Realm realm) {
                return realm.where(RealmChapter.class).findAll(); // TODO: filter by novelId
            }
        }).map(new Func1<RealmResults<RealmChapter>, List<Chapter>>() {
            @Override
            public List<Chapter> call(RealmResults<RealmChapter> realmChapters) {
                final List<Chapter> chapters = new ArrayList<>(realmChapters.size());
                for (RealmChapter realmChapter : realmChapters)
                    chapters.add(chapterFromRealm(realmChapter));
                return chapters;
            }
        });
    }

    @Override
    public Observable<ResourceCount> chaptersCount() {
        return getRealmNovelChapters().map(new Func1<RealmResults<RealmChapter>, ResourceCount>() {
            @Override
            public ResourceCount call(RealmResults<RealmChapter> realmChapters) {
                return new ResourceCount.Builder().count(realmChapters.size()).build();
            }
        });
    }

    @Override
    public Observable<Chapter> saveChapter(Chapter chapter) {
        return null;
    }
}

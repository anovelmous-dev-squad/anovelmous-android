package com.anovelmous.app.data.api;

import android.content.Context;

import com.anovelmous.app.data.api.model.RealmChapter;
import com.anovelmous.app.data.api.model.RealmFormattedNovelToken;
import com.anovelmous.app.data.api.model.RealmNovel;
import com.anovelmous.app.data.api.model.RealmVote;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Vote;
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
    public Observable<List<Chapter>> chapters(final long novelId) {
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
    public Observable<Chapter> saveChapter(final Chapter chapter) {
        return RealmObservable.object(context, new Func1<Realm, RealmChapter>() {
            @Override
            public RealmChapter call(Realm realm) {
                RealmChapter realmChapter = new RealmChapter(chapter, realm);
                return realm.copyToRealmOrUpdate(realmChapter);
            }
        }).map(new Func1<RealmChapter, Chapter>() {
            @Override
            public Chapter call(RealmChapter realmChapter) {
                return chapterFromRealm(realmChapter);
            }
        });
    }

    private static FormattedNovelToken formattedNovelTokenFromRealm(RealmFormattedNovelToken formattedNovelToken) {
        return new FormattedNovelToken.Builder()
                .id(formattedNovelToken.getId())
                .url(formattedNovelToken.getUrl())
                .content(formattedNovelToken.getContent())
                .ordinal(formattedNovelToken.getOrdinal())
                .chapter(formattedNovelToken.getChapter().getUrl())
                .createdAt(new DateTime(formattedNovelToken.getCreatedAt()))
                .build();
    }

    @Override
    public Observable<List<FormattedNovelToken>> chapterText(final long chapterId) {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmFormattedNovelToken>>() {
            @Override
            public RealmResults<RealmFormattedNovelToken> call(Realm realm) {
                return realm.where(RealmFormattedNovelToken.class)
                        .equalTo("chapter.id", chapterId).findAll();
            }
        }).map(new Func1<RealmResults<RealmFormattedNovelToken>, List<FormattedNovelToken>>() {
            @Override
            public List<FormattedNovelToken> call(RealmResults<RealmFormattedNovelToken> realmFormattedNovelTokens) {
                final List<FormattedNovelToken> chapterText = new ArrayList<>(realmFormattedNovelTokens.size());
                for (RealmFormattedNovelToken token : realmFormattedNovelTokens)
                    chapterText.add(formattedNovelTokenFromRealm(token));
                return chapterText;
            }
        });
    }

    @Override
    public Observable<ResourceCount> chapterTextTokenCount() {
        return null;
    }

    @Override
    public Observable<FormattedNovelToken> saveFormattedNovelToken(FormattedNovelToken formattedNovelToken) {
        return null;
    }

    public static Vote voteFromRealm(RealmVote vote) {
        return new Vote.Builder()
                .id(vote.getId())
                .url(vote.getUrl())
                .token(vote.getToken().getUrl())
                .ordinal(vote.getOrdinal())
                .selected(vote.isSelected())
                .chapter(vote.getChapter().getUrl())
                .user(vote.getUser().getUrl())
                .createdAt(new DateTime(vote.getCreatedAt()))
                .build();
    }

    @Override
    public Observable<Vote> saveVote(final Vote vote, final RestVerb restVerb) {
        return RealmObservable.object(context, new Func1<Realm, RealmVote>() {
            @Override
            public RealmVote call(Realm realm) {
                RealmVote realmVote = new RealmVote(vote, realm, restVerb);
                return realm.copyToRealm(realmVote);
            }
        }).map(new Func1<RealmVote, Vote>() {
            @Override
            public Vote call(RealmVote realmVote) {
                return voteFromRealm(realmVote);
            }
        });
    }
}

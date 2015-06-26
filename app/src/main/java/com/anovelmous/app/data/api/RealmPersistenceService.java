package com.anovelmous.app.data.api;

import android.content.Context;

import com.anovelmous.app.data.api.model.RealmChapter;
import com.anovelmous.app.data.api.model.RealmFormattedNovelToken;
import com.anovelmous.app.data.api.model.RealmGroup;
import com.anovelmous.app.data.api.model.RealmNovel;
import com.anovelmous.app.data.api.model.RealmToken;
import com.anovelmous.app.data.api.model.RealmUser;
import com.anovelmous.app.data.api.model.RealmVote;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Group;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.User;
import com.anovelmous.app.data.api.resource.Vote;
import com.anovelmous.app.data.api.rx.RealmObservable;
import com.anovelmous.app.util.NotUniqueException;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

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
    public Observable<Chapter> chapter(final long chapterId) {
        return RealmObservable.object(context, new Func1<Realm, RealmChapter>() {
            @Override
            public RealmChapter call(Realm realm) {
                return realm.where(RealmChapter.class).equalTo("id", chapterId).findFirst();
            }
        }).map(new Func1<RealmChapter, Chapter>() {
            @Override
            public Chapter call(RealmChapter realmChapter) {
                return chapterFromRealm(realmChapter);
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
                .restVerb(RestVerb.GET)
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

    private Observable<RealmResults<RealmChapter>> getRealmNovelChapters(final long novelId) {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmChapter>>() {
            @Override
            public RealmResults<RealmChapter> call(Realm realm) {
                return realm.where(RealmChapter.class).equalTo("novel.id", novelId).findAll();
            }
        });
    }

    private Observable<RealmResults<RealmChapter>> getRealmChapters() {
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
                .restVerb(RestVerb.GET)
                .title(realmChapter.getTitle())
                .isCompleted(realmChapter.isCompleted())
                .votingDuration(realmChapter.getVotingDuration())
                .novel(realmChapter.getNovel().getUrl())
                .createdAt(new DateTime(realmChapter.getCreatedAt()))
                .build();
    }

    @Override
    public Observable<List<Chapter>> chapters(final long novelId) {
        return getRealmNovelChapters(novelId).map(new Func1<RealmResults<RealmChapter>, List<Chapter>>() {
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
        return getRealmChapters().map(new Func1<RealmResults<RealmChapter>, ResourceCount>() {
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
                .restVerb(RestVerb.GET)
                .content(formattedNovelToken.getContent())
                .ordinal(formattedNovelToken.getOrdinal())
                .chapter(formattedNovelToken.getChapter().getUrl())
                .createdAt(new DateTime(formattedNovelToken.getCreatedAt()))
                .build();
    }

    private Observable<RealmResults<RealmFormattedNovelToken>> getRealmChapterText(final long chapterId) {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmFormattedNovelToken>>() {
            @Override
            public RealmResults<RealmFormattedNovelToken> call(Realm realm) {
                return realm.where(RealmFormattedNovelToken.class)
                        .equalTo("chapter.id", chapterId).findAll();
            }
        });
    }

    @Override
    public Observable<List<FormattedNovelToken>> chapterText(final long chapterId) {
        return getRealmChapterText(chapterId)
                .map(new Func1<RealmResults<RealmFormattedNovelToken>, List<FormattedNovelToken>>() {
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
    public Observable<ResourceCount> chapterTextTokenCount(final long chapterId) {
        return getRealmChapterText(chapterId)
                .map(new Func1<RealmResults<RealmFormattedNovelToken>, ResourceCount>() {
                    @Override
                    public ResourceCount call(RealmResults<RealmFormattedNovelToken> realmFormattedNovelTokens) {
                        return new ResourceCount.Builder()
                                .count(realmFormattedNovelTokens.size()).build();
                    }
                });
    }

    @Override
    public Observable<FormattedNovelToken> saveFormattedNovelToken(FormattedNovelToken formattedNovelToken) {
        return null;
    }


    private static Token tokenFromRealm(RealmToken realmToken) {
        return new Token.Builder()
                .id(realmToken.getId())
                .url(realmToken.getUrl())
                .restVerb(RestVerb.valueOf(realmToken.getRestVerb()))
                .content(realmToken.getContent())
                .isPunctuation(realmToken.isPunctuation())
                .isValid(realmToken.isValid())
                .createdAt(new DateTime(realmToken.getCreatedAt()))
                .build();
    }

    @Override
    public Observable<List<Token>> tokens() {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmToken>>() {
            @Override
            public RealmResults<RealmToken> call(Realm realm) {
                return realm.where(RealmToken.class).findAll();
            }
        }).map(new Func1<RealmResults<RealmToken>, List<Token>>() {
            @Override
            public List<Token> call(RealmResults<RealmToken> realmTokens) {
                List<Token> tokens = new ArrayList<>(realmTokens.size());
                for (RealmToken realmToken : realmTokens)
                    tokens.add(tokenFromRealm(realmToken));
                return tokens;
            }
        });
    }

    @Override
    public Observable<List<Token>> saveTokens(final List<Token> tokens) {
        return RealmObservable.list(context, new Func1<Realm, RealmList<RealmToken>>() {
            @Override
            public RealmList<RealmToken> call(Realm realm) {
                List<RealmToken> realmTokens = new ArrayList<>(tokens.size());
                for (Token token : tokens)
                    realmTokens.add(new RealmToken(token));
                realmTokens = realm.copyToRealmOrUpdate(realmTokens);

                RealmList<RealmToken> results = new RealmList<>();
                results.addAll(realmTokens);
                return results;
            }
        }).map(new Func1<RealmList<RealmToken>, List<Token>>() {
            @Override
            public List<Token> call(RealmList<RealmToken> realmTokens) {
                List<Token> tokens = new ArrayList<>(realmTokens.size());
                for (RealmToken realmToken : realmTokens)
                    tokens.add(tokenFromRealm(realmToken));
                return tokens;
            }
        });
    }

    @Override
    public Observable<Token> saveToken(final Token token) {
        return RealmObservable.object(context, new Func1<Realm, RealmToken>() {
            @Override
            public RealmToken call(Realm realm) {
                RealmToken realmToken = new RealmToken(token);
                return realm.copyToRealmOrUpdate(realmToken);
            }
        }).map(new Func1<RealmToken, Token>() {
            @Override
            public Token call(RealmToken realmToken) {
                return tokenFromRealm(realmToken);
            }
        });
    }

    @Override
    public Observable<ResourceCount> tokensCount() {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmToken>>() {
            @Override
            public RealmResults<RealmToken> call(Realm realm) {
                return realm.where(RealmToken.class).findAll();
            }
        }).map(new Func1<RealmResults<RealmToken>, ResourceCount>() {
            @Override
            public ResourceCount call(RealmResults<RealmToken> realmTokens) {
                return new ResourceCount.Builder().count(realmTokens.size()).build();
            }
        });
    }

    @Override
    public Observable<Token> lookupTokenByContent(final String content) {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmToken>>() {
            @Override
            public RealmResults<RealmToken> call(Realm realm) {
                return realm.where(RealmToken.class).equalTo("content", content).findAll();
            }
        }).map(new Func1<RealmResults<RealmToken>, Token>() {
            @Override
            public Token call(RealmResults<RealmToken> realmTokens) {
                try {
                    if (realmTokens.size() > 1)
                        throw new NotUniqueException("Realm Token content is not unique");
                } catch (NotUniqueException e) {
                    Timber.e(e.getMessage());
                }

                return tokenFromRealm(realmTokens.first());
            }
        });
    }

    public static Vote voteFromRealm(RealmVote vote) {
        return new Vote.Builder()
                .id(vote.getId())
                .url(vote.getUrl())
                .restVerb(RestVerb.valueOf(vote.getRestVerb()))
                .token(vote.getToken().getUrl())
                .ordinal(vote.getOrdinal())
                .selected(vote.isSelected())
                .chapter(vote.getChapter().getUrl())
                .user(vote.getUser().getUrl())
                .createdAt(new DateTime(vote.getCreatedAt()))
                .build();
    }

    @Override
    public Observable<Vote> saveVote(final Vote vote) {
        return RealmObservable.object(context, new Func1<Realm, RealmVote>() {
            @Override
            public RealmVote call(Realm realm) {
                RealmVote realmVote = new RealmVote(vote, realm);
                return realm.copyToRealm(realmVote);
            }
        }).map(new Func1<RealmVote, Vote>() {
            @Override
            public Vote call(RealmVote realmVote) {
                return voteFromRealm(realmVote);
            }
        });
    }

    private static Group groupFromRealm(RealmGroup realmGroup) {
        return new Group.Builder()
                .id(realmGroup.getId())
                .url(realmGroup.getUrl())
                .name(realmGroup.getName())
                .restVerb(RestVerb.valueOf(realmGroup.getRestVerb()))
                .build();
    }

    private static User userFromRealm(RealmUser realmUser) {
        List<String> groupsUrlList = new ArrayList<>(realmUser.getGroups().size());
        for (RealmGroup realmGroup : realmUser.getGroups())
            groupsUrlList.add(realmGroup.getUrl());

        return new User.Builder()
                .id(realmUser.getId())
                .url(realmUser.getUrl())
                .restVerb(RestVerb.valueOf(realmUser.getRestVerb()))
                .username(realmUser.getUsername())
                .email(realmUser.getEmail())
                .groups(groupsUrlList)
                .dateJoined(new DateTime(realmUser.getDateJoined()))
                .build();
    }

    @Override
    public Observable<User> getMyUser(final String authToken) {
        return RealmObservable.results(context, new Func1<Realm, RealmResults<RealmUser>>() {
            @Override
            public RealmResults<RealmUser> call(Realm realm) {
                return realm.where(RealmUser.class).equalTo("authToken", authToken).findAll();
            }
        }).map(new Func1<RealmResults<RealmUser>, User>() {
            @Override
            public User call(RealmResults<RealmUser> realmUsers) {
                User user = userFromRealm(realmUsers.first());
                return user;
            }
        });
    }
}

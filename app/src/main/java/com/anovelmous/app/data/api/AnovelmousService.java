package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.NovelToken;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.User;
import com.anovelmous.app.data.api.resource.Vote;
import com.anovelmous.app.data.api.transforms.SearchResultToChapterList;
import com.anovelmous.app.data.api.transforms.SearchResultToFormattedNovelTokenList;
import com.anovelmous.app.data.api.transforms.SearchResultToNovelList;
import com.facebook.AccessToken;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import timber.log.Timber;

/**
 * Created by Greg Ziegan on 6/9/15.
 */
public class AnovelmousService implements RestService {
    private NetworkService networkService;
    private PersistenceService persistenceService;

    public AnovelmousService(NetworkService networkService, PersistenceService persistenceService) {
        this.networkService = networkService;
        this.persistenceService = persistenceService;
    }

    private final Func2<ResourceCount, ResourceCount, Boolean> hasRemoteResourceCountChange =
            new Func2<ResourceCount, ResourceCount, Boolean>() {
                @Override
                public Boolean call(ResourceCount remoteRes, ResourceCount localRes) {
                    return remoteRes.count != localRes.count;
                }
            };

    @Override
    public Observable<List<Novel>> getAllNovels() {
        return Observable.combineLatest(
                networkService.novelsCount(), persistenceService.novelsCount(),
                hasRemoteResourceCountChange)
                .flatMap(new Func1<Boolean, Observable<List<Novel>>>() {
                    @Override
                    public Observable<List<Novel>> call(Boolean needsUpdating) {
                        if (needsUpdating) {
                             return networkService.novels(100, 1, Sort.CREATED_AT, Order.DESC)
                                    .map(SearchResultToNovelList.instance())
                                    .map(cacheNovels);
                        } else {
                            return persistenceService.novels();
                        }
                    }
                });
    }

    private final Func1<List<Novel>, List<Novel>> cacheNovels = new Func1<List<Novel>, List<Novel>>() {
        @Override
        public List<Novel> call(List<Novel> novels) {
            for (Novel novel : novels)
                persistenceService.saveNovel(novel)
                        .subscribe(new Action1<Novel>() {
                            @Override
                            public void call(Novel novel) {
                                Timber.v("Saved novel: " + novel.toString());
                            }
                        });
            return novels;
        }
    };

    @Override
    public Observable<Chapter> getChapter(long chapterId) {
        return persistenceService.chapter(chapterId); // TODO: check remote resource's "last_modified" timestamp (when it exists)
    }

    @Override
    public Observable<List<Chapter>> getNovelChapters(final long novelId) {
        return Observable.combineLatest(
                networkService.chaptersCount(), persistenceService.chaptersCount(),
                hasRemoteResourceCountChange)
                .flatMap(new Func1<Boolean, Observable<List<Chapter>>>() {
                    @Override
                    public Observable<List<Chapter>> call(Boolean needsUpdating) {
                        if (needsUpdating) {
                            return networkService.chapters(novelId, Sort.CREATED_AT, Order.DESC)
                                    .map(SearchResultToChapterList.instance())
                                    .map(cacheChapters);

                        } else {
                            return persistenceService.chapters(novelId);
                        }
                    }
                });
    }

    private final Func1<List<Chapter>, List<Chapter>> cacheChapters = new Func1<List<Chapter>, List<Chapter>>() {
        @Override
        public List<Chapter> call(List<Chapter> chapters) {
            for (Chapter chapter : chapters)
                persistenceService.saveChapter(chapter).subscribe(new Action1<Chapter>() {
                    @Override
                    public void call(Chapter chapter) {
                        Timber.v("Successfully saved: " + chapter.toString());
                    }
                });
            return chapters;
        }
    };

    public Observable<List<FormattedNovelToken>> getChapterText(final long chapterId) {
        return Observable.combineLatest(
                networkService.chapterTextTokenCount(chapterId),
                persistenceService.chapterTextTokenCount(chapterId),
                hasRemoteResourceCountChange)
                .flatMap(new Func1<Boolean, Observable<List<FormattedNovelToken>>>() {
                    @Override
                    public Observable<List<FormattedNovelToken>> call(Boolean needsUpdating) {
                        if (needsUpdating) {
                            return networkService.chapterText(chapterId, Sort.CREATED_AT, Order.ASC)
                                    .map(SearchResultToFormattedNovelTokenList.instance())
                                    .map(cacheFormattedNovelTokens);
                        } else {
                            return persistenceService.chapterText(chapterId);
                        }
                    }
                });
    }

    private final Func1<List<FormattedNovelToken>, List<FormattedNovelToken>> cacheFormattedNovelTokens = new Func1<List<FormattedNovelToken>, List<FormattedNovelToken>>() {
        @Override
        public List<FormattedNovelToken> call(List<FormattedNovelToken> formattedNovelTokens) {
            for (FormattedNovelToken token : formattedNovelTokens)
                persistenceService.saveFormattedNovelToken(token).subscribe(new Action1<FormattedNovelToken>() {
                    @Override
                    public void call(FormattedNovelToken formattedNovelToken) {
                        Timber.v("Successfully saved: " + formattedNovelToken.toString());
                    }
                });
            return formattedNovelTokens;
        }
    };

    @Override
    public Observable<List<Token>> getAllTokens() {
        return Observable.combineLatest(
                networkService.tokensCount(),
                persistenceService.tokensCount(),
                hasRemoteResourceCountChange
        ).flatMap(new Func1<Boolean, Observable<List<Token>>>() {
            @Override
            public Observable<List<Token>> call(Boolean needsUpdating) {
                if (needsUpdating) {
                    return networkService.tokens(Sort.CREATED_AT, Order.DESC)
                            .map(cacheTokens);
                } else {
                    return persistenceService.tokens();
                }
            }
        });
    }

    private final Func1<List<Token>, List<Token>> cacheTokens = new Func1<List<Token>, List<Token>>() {
        @Override
        public List<Token> call(List<Token> tokens) {
            for (Token token : tokens)
                persistenceService.saveToken(token).subscribe(new Action1<Token>() {
                    @Override
                    public void call(Token token) {
                        Timber.v("Successfully saved: " + token.toString());
                    }
                });
            return tokens;
        }
    };

    @Override
    public Observable<Token> getTokenFromContent(final String content) {
        return persistenceService.lookupTokenByContent(content).flatMap(new Func1<Token, Observable<Token>>() {
            @Override
            public Observable<Token> call(Token token) {
                if (token == null)
                    return networkService.getTokenByContent(content);
                else
                    return Observable.just(token);
            }
        });
    }

    public Observable<Vote> castVote(Vote vote) {
        // TODO: check response vote and update flags
        return persistenceService.saveVote(vote)
                .flatMap(new Func1<Vote, Observable<Vote>>() {
                    @Override
                    public Observable<Vote> call(Vote vote) {
                        return networkService.postVote(vote);
                    }
                });
    }

    @Override
    public Observable<List<String>> getGrammarFilteredStrings() {
        return networkService.getGrammarFilteredWords();
    }

    @Override
    public Observable<User> getUser(String authToken) {
        return persistenceService.getUser(authToken).map(new Func1<User, User>() {
            @Override
            public User call(User user) {
                if (user != null)
                    return user;  // TODO: create lastModified field and refresh cache
                return user; // TODO: either lookup user remotely or POST
            }
        });
    }

    @Override
    public Observable<User> getUser(AccessToken accessToken) {
        return persistenceService.getUser(accessToken);
    }

    @Override
    public Observable<User> getUser(long userId) {
        return persistenceService.getUser(userId); // TODO: finish implementation
    }

    @Override
    public Observable<User> createUser(User user) {
        return persistenceService.createUser(user)
                .flatMap(new Func1<User, Observable<User>>() {
                    @Override
                    public Observable<User> call(User user) {
                        return networkService.createUser(user);
                    }
                }).flatMap(new Func1<User, Observable<User>>() {
                    @Override
                    public Observable<User> call(User user) {
                        return persistenceService.updateUser(user);
                    }
                });
    }

    @Override
    public Observable<NovelToken> getMostRecentNovelToken(long chapterId) {
        return networkService.getMostRecentNovelToken(chapterId, Sort.CREATED_AT, Order.DESC); // TODO: maybe implement smart polling/caching?
    }
}

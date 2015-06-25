package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.User;
import com.anovelmous.app.data.api.resource.Vote;
import com.anovelmous.app.data.api.transforms.SearchResultToChapterList;
import com.anovelmous.app.data.api.transforms.SearchResultToFormattedNovelTokenList;
import com.anovelmous.app.data.api.transforms.SearchResultToNovelList;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

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
                            Observable<List<Novel>> novelsStream = networkService.
                                    novels(100, 1, Sort.CREATED_AT, Order.DESC)
                                    .map(SearchResultToNovelList.instance());

                            // Persist new novels
                            novelsStream.subscribe(new Action1<List<Novel>>() {
                                @Override
                                public void call(List<Novel> novels) {
                                    for (Novel novel : novels)
                                        persistenceService.saveNovel(novel);
                                }
                            });
                            return novelsStream;
                        } else {
                            return persistenceService.novels();
                        }
                    }
                });
    }

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
                            Observable<List<Chapter>> chaptersStream = networkService.
                                    chapters(novelId, Sort.CREATED_AT, Order.DESC)
                                    .map(SearchResultToChapterList.instance());

                            chaptersStream.subscribe(new Action1<List<Chapter>>() {
                                @Override
                                public void call(List<Chapter> chapters) {
                                    for (Chapter chapter : chapters)
                                        persistenceService.saveChapter(chapter);
                                }
                            });
                            return chaptersStream;
                        } else {
                            return persistenceService.chapters(novelId);
                        }
                    }
                });
    }

    public Observable<List<FormattedNovelToken>> getChapterText(final long chapterId) {
        return Observable.combineLatest(
                networkService.chapterTextTokenCount(chapterId),
                persistenceService.chapterTextTokenCount(chapterId),
                hasRemoteResourceCountChange)
                .flatMap(new Func1<Boolean, Observable<List<FormattedNovelToken>>>() {
                    @Override
                    public Observable<List<FormattedNovelToken>> call(Boolean needsUpdating) {
                        if (needsUpdating) {
                            Observable<List<FormattedNovelToken>> chapterTextStream = networkService
                                    .chapterText(chapterId, Sort.CREATED_AT, Order.ASC)
                                    .map(SearchResultToFormattedNovelTokenList.instance());

                            chapterTextStream.subscribe(new Action1<List<FormattedNovelToken>>() {
                                @Override
                                public void call(List<FormattedNovelToken> formattedNovelTokens) {
                                    for (FormattedNovelToken token : formattedNovelTokens)
                                        persistenceService.saveFormattedNovelToken(token);
                                }
                            });
                            return chapterTextStream;
                        } else {
                            return persistenceService.chapterText(chapterId);
                        }
                    }
                });
    }

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
                    Observable<List<Token>> tokensStream = networkService
                            .tokens(Sort.CREATED_AT, Order.DESC);

                    tokensStream.subscribe(new Action1<List<Token>>() {
                        @Override
                        public void call(List<Token> tokens) {
                            persistenceService.saveTokens(tokens, RestVerb.GET);
                        }
                    });
                    return tokensStream;
                } else {
                    return persistenceService.tokens();
                }
            }
        });
    }

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
        return persistenceService.saveVote(vote, RestVerb.POST)
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
    public Observable<User> getMyUser(String authToken) {
        return persistenceService.getMyUser(authToken).map(new Func1<User, User>() {
            @Override
            public User call(User user) {
                if (user != null)
                    return user;  // TODO: create lastModified field and refresh cache

                return user; // TODO: either lookup user remotely or POST
            }
        });
    }
}

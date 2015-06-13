package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Vote;
import com.anovelmous.app.data.api.transforms.SearchResultToChapterList;
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

    public Observable<List<FormattedNovelToken>> getChapterText(long chapterId) {
        return null;
    }

    public Observable<Vote> castVote(Vote vote) {
        return persistenceService.saveVote(vote)
                .map(new Func1<Vote, Vote>() {
                    @Override
                    public Vote call(Vote vote) {
                        return null; // TODO: cast vote
                    }
                });
    }
}

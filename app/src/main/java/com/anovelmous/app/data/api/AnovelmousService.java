package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.transforms.SearchResultToNovelList;

import java.util.List;

import rx.Observable;
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

    public Observable<List<Novel>> getAllNovels() {
        return Observable.combineLatest(networkService.novelsCount(), persistenceService.novelsCount(),
                new Func2<ResourceCount, ResourceCount, Boolean>() {
                    @Override
                    public Boolean call(ResourceCount resourceCount, ResourceCount resourceCount2) {
                        return resourceCount.count != resourceCount2.count;
                    }
                })
                .flatMap(new Func1<Boolean, Observable<List<Novel>>>() {
                    @Override
                    public Observable<List<Novel>> call(Boolean needsUpdating) {
                        if (needsUpdating) {
                            return networkService.novels(100, 1, Sort.CREATED_AT, Order.DESC)
                                    .map(SearchResultToNovelList.instance());
                        } else {
                            return persistenceService.novels();
                        }
                    }
                });
    }

    public List<Chapter> getNovelChapters(long novelId) {
        return null;
    }

    List<FormattedNovelToken> getChapterText(long chapterId) {
        return null;
    }
}

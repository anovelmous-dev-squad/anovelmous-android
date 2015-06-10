package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Novel;
import com.anovelmous.app.data.api.resource.ResourceCount;

import java.util.List;

import rx.Observable;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/8/15.
 */
public interface PersistenceService {
    Observable<List<Novel>> novels();
    Observable<ResourceCount> novelsCount();
    Observable<Novel> saveNovel(Novel novel);
}

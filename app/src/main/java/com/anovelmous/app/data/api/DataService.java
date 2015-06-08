package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Novel;

import java.util.List;

import rx.Observable;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/8/15.
 */
public interface DataService {
    public Observable<List<Novel>> novels();
    public Observable<Novel> saveNovel(Novel novel);
}

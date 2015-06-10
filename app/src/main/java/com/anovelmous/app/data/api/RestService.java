package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Novel;

import java.util.List;

import rx.Observable;

/**
 * Created by Greg Ziegan on 6/10/15.
 */
public interface RestService {
    Observable<List<Novel>> getAllNovels();
}

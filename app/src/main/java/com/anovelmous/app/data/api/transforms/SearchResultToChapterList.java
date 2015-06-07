package com.anovelmous.app.data.api.transforms;

import com.anovelmous.app.data.api.resource.Chapter;
import com.anovelmous.app.data.api.resource.ChaptersResponse;

import java.util.Collections;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by Greg Ziegan on 6/4/15.
 */
public final class SearchResultToChapterList implements Func1<ChaptersResponse, List<Chapter>> {
    private static volatile SearchResultToChapterList instance;

    public static SearchResultToChapterList instance() {
        if (instance == null) {
            instance = new SearchResultToChapterList();
        }
        return instance;
    }

    @Override public List<Chapter> call(ChaptersResponse chaptersResponse) {
        return chaptersResponse.items == null //
                ? Collections.<Chapter>emptyList() //
                : chaptersResponse.items;
    }
}

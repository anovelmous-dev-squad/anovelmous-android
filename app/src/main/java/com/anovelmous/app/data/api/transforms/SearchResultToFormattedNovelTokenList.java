package com.anovelmous.app.data.api.transforms;

import com.anovelmous.app.data.api.resource.FormattedNovelToken;
import com.anovelmous.app.data.api.response.ChapterTextResponse;

import java.util.Collections;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by Greg Ziegan on 6/4/15.
 */
public final class SearchResultToFormattedNovelTokenList
        implements Func1<ChapterTextResponse, List<FormattedNovelToken>> {
    private static volatile SearchResultToFormattedNovelTokenList instance;

    public static SearchResultToFormattedNovelTokenList instance() {
        if (instance == null) {
            instance = new SearchResultToFormattedNovelTokenList();
        }
        return instance;
    }

    @Override public List<FormattedNovelToken> call(ChapterTextResponse chapterTextResponse) {
        return chapterTextResponse.items == null
                ? Collections.<FormattedNovelToken>emptyList()
                : chapterTextResponse.items;
    }
}

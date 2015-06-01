package com.anovelmous.app.data.api.model;

import java.util.List;

/**
 * Created by Greg Ziegan on 6/2/15.
 */
public final class ChapterTextResponse {
    public final List<FormattedNovelToken> text;

    public ChapterTextResponse(List<FormattedNovelToken> text) {
        this.text = text;
    }
}

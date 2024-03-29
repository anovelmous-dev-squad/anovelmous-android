package com.anovelmous.app.data.api.response;

import com.anovelmous.app.data.api.resource.Chapter;

import java.util.List;

/**
 * Created by Greg Ziegan on 6/2/15.
 */
public final class ChaptersResponse {
    public final List<Chapter> items;

    public ChaptersResponse(List<Chapter> items) {
        this.items = items;
    }
}

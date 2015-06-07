package com.anovelmous.app.data.api.resource;

import java.util.List;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public final class NovelsResponse {
    public final List<Novel> items;

    public NovelsResponse(List<Novel> items) {
        this.items = items;
    }
}

package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.NovelsResponse;

import java.util.Arrays;

import static com.anovelmous.app.data.api.MockNovels.AMERICAN_GODS;
import static com.anovelmous.app.data.api.MockNovels.HUCK_FINN;
import static com.anovelmous.app.data.api.MockNovels.WAR_AND_PEACE;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public enum MockNovelsResponse {
    SUCCESS("Success", new NovelsResponse(Arrays.asList( //
            AMERICAN_GODS, //
            HUCK_FINN, //
            WAR_AND_PEACE))),
    EMPTY("Empty", new NovelsResponse(null));

    public final String name;
    public final NovelsResponse response;

    MockNovelsResponse(String name, NovelsResponse response) {
        this.name = name;
        this.response = response;
    }

    @Override public String toString() {
        return name;
    }
}

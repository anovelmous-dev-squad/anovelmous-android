package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.response.ChapterTextResponse;

import java.util.Arrays;

import static com.anovelmous.app.data.api.MockChapterText.*;
/**
 * Created by Greg Ziegan on 6/2/15.
 */
public enum MockChapterTextResponse {
    SUCCESS("Success", new ChapterTextResponse(Arrays.asList(
            DADDY,
            YOUR2,
            IS,
            WHO,
            NOW,
            LEAVE,
            PLEASE2,
            YOU,
            LOVE,
            I,
            FACE,
            MY,
            EAT,
            PURITY,
            YOUR,
            SACRIFICE,
            DONT,
            STOP,
            AWAY,
            GO,
            PLEASE))),
    EMPTY("Empty", new ChapterTextResponse(null));

    public final String name;
    public final ChapterTextResponse response;

    MockChapterTextResponse(String name, ChapterTextResponse response) {
        this.name = name;
        this.response = response;
    }

    @Override public String toString() {
        return name;
    }
}

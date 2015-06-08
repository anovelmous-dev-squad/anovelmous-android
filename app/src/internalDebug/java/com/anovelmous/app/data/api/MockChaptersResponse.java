package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.response.ChaptersResponse;

import java.util.Arrays;

import static com.anovelmous.app.data.api.MockChapters.PROLOGUE;
import static com.anovelmous.app.data.api.MockChapters.END;
import static com.anovelmous.app.data.api.MockChapters.ALMOST;
import static com.anovelmous.app.data.api.MockChapters.MIDDLE;
import static com.anovelmous.app.data.api.MockChapters.BEGINNING;
import static com.anovelmous.app.data.api.MockChapters.INTRODUCTION;
import static com.anovelmous.app.data.api.MockChapters.FIN;
import static com.anovelmous.app.data.api.MockChapters.LOSING;
import static com.anovelmous.app.data.api.MockChapters.WINNING;
import static com.anovelmous.app.data.api.MockChapters.OPENING;

/**
 * Created by Greg Ziegan on 6/2/15.
 */
public enum MockChaptersResponse {
    SUCCESS("Success", new ChaptersResponse(Arrays.asList( //
            PROLOGUE,
            END,
            ALMOST,
            MIDDLE,
            BEGINNING,
            INTRODUCTION,
            FIN,
            LOSING,
            WINNING,
            OPENING))),
    EMPTY("Empty", new ChaptersResponse(null));

    public final String name;
    public final ChaptersResponse response;

    MockChaptersResponse(String name, ChaptersResponse response) {
        this.name = name;
        this.response = response;
    }

    @Override public String toString() {
        return name;
    }
}

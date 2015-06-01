package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.model.Novel;

import org.joda.time.DateTime;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
final class MockNovels {
    static final Novel AMERICAN_GODS = new Novel.Builder()
            .name("American Gods")
            .updatedAt(new DateTime())
            .build();
    static final Novel HUCK_FINN = new Novel.Builder()
            .name("Huck Finn")
            .updatedAt(new DateTime().minusDays(1))
            .build();
    static final Novel WAR_AND_PEACE = new Novel.Builder()
            .name("War & Peace")
            .updatedAt(new DateTime().minusDays(2))
            .build();

    private MockNovels() {
        throw new AssertionError("No instances.");
    }
}

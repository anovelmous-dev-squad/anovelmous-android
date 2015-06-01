package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.model.Novel;

import org.joda.time.DateTime;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
final class MockNovels {
    static final Novel AMERICAN_GODS = new Novel.Builder()
            .title("American Gods")
            .isCompleted(false)
            .votingDuration(15)
            .createdAt(new DateTime())
            .build();
    static final Novel HUCK_FINN = new Novel.Builder()
            .title("Huck Finn")
            .isCompleted(true)
            .votingDuration(10)
            .createdAt(new DateTime().minusDays(6))
            .build();
    static final Novel WAR_AND_PEACE = new Novel.Builder()
            .title("War & Peace")
            .isCompleted(true)
            .votingDuration(20)
            .createdAt(new DateTime().minusDays(20))
            .build();

    private MockNovels() {
        throw new AssertionError("No instances.");
    }
}

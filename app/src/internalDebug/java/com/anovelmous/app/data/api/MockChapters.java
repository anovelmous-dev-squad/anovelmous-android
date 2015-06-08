package com.anovelmous.app.data.api;


import com.anovelmous.app.data.api.resource.Chapter;

import org.joda.time.DateTime;

/**
 * Created by Greg Ziegan on 6/2/15.
 */
final class MockChapters {
    static final Chapter PROLOGUE = new Chapter.Builder()
            .id(10)
            .url("mock://chapters/10")
            .title("Prologue")
            .isCompleted(false)
            .votingDuration(15)
            .novel("mock://novels/3")
            .createdAt(new DateTime().minusHours(2))
            .build();
    static final Chapter END = new Chapter.Builder()
            .id(9)
            .url("mock://chapters/9")
            .title("End")
            .isCompleted(true)
            .votingDuration(10)
            .novel("mock://novels/2")
            .createdAt(new DateTime().minusDays(1))
            .build();
    static final Chapter ALMOST = new Chapter.Builder()
            .id(8)
            .url("mock://chapters/8")
            .title("Almost")
            .isCompleted(true)
            .votingDuration(10)
            .novel("mock://novels/2")
            .createdAt(new DateTime().minusDays(2))
            .build();
    static final Chapter MIDDLE = new Chapter.Builder()
            .id(7)
            .url("mock://chapters/7")
            .title("Middle")
            .isCompleted(true)
            .votingDuration(10)
            .novel("mock://novels/2")
            .createdAt(new DateTime().minusDays(2).minusHours(3))
            .build();
    static final Chapter BEGINNING = new Chapter.Builder()
            .id(6)
            .url("mock://chapters/6")
            .title("Beginning")
            .isCompleted(true)
            .votingDuration(10)
            .novel("mock://novels/2")
            .createdAt(new DateTime().minusDays(2).minusHours(8))
            .build();
    static final Chapter INTRODUCTION = new Chapter.Builder()
            .id(5)
            .url("mock://chapters/5")
            .title("Introduction")
            .isCompleted(true)
            .votingDuration(10)
            .novel("mock://novels/2")
            .createdAt(new DateTime().minusDays(2).minusHours(13))
            .build();
    static final Chapter FIN = new Chapter.Builder()
            .id(4)
            .url("mock://chapters/4")
            .title("Fin")
            .isCompleted(true)
            .votingDuration(20)
            .novel("mock://novels/1")
            .createdAt(new DateTime().minusDays(7).minusHours(1))
            .build();
    static final Chapter LOSING = new Chapter.Builder()
            .id(3)
            .url("mock://chapters/3")
            .title("Losing")
            .isCompleted(true)
            .votingDuration(20)
            .novel("mock://novels/1")
            .createdAt(new DateTime().minusDays(7).minusHours(3))
            .build();
    static final Chapter WINNING = new Chapter.Builder()
            .id(2)
            .url("mock://chapters/2")
            .title("Winning")
            .isCompleted(true)
            .votingDuration(20)
            .novel("mock://novels/1")
            .createdAt(new DateTime().minusDays(7).minusHours(5))
            .build();
    static final Chapter OPENING = new Chapter.Builder()
            .id(1)
            .url("mock://chapters/1")
            .title("Opening")
            .isCompleted(true)
            .votingDuration(20)
            .novel("mock://novels/1")
            .createdAt(new DateTime().minusDays(7).minusHours(8))
            .build();

    private MockChapters() {
        throw new AssertionError("No instances.");
    }
}

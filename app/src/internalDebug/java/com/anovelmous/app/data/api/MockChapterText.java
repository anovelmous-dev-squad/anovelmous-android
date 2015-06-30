package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.FormattedNovelToken;

import org.joda.time.DateTime;

/**
 * Created by Greg Ziegan on 6/2/15.
 */
final class MockChapterText {
    static final FormattedNovelToken DADDY = new FormattedNovelToken.Builder()
            .id("21")
            .url("mock://formatted_novel_tokens/21")
            .restVerb(RestVerb.GET)
            .content("daddy?")
            .ordinal(4)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(30))
            .build();

    static final FormattedNovelToken YOUR2 = new FormattedNovelToken.Builder()
            .id("20")
            .url("mock://formatted_novel_tokens/20")
            .restVerb(RestVerb.GET)
            .content("your")
            .ordinal(3)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(32))
            .build();

    static final FormattedNovelToken IS = new FormattedNovelToken.Builder()
            .id("19")
            .url("mock://formatted_novel_tokens/19")
            .restVerb(RestVerb.GET)
            .content("is")
            .ordinal(2)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(33))
            .build();

    static final FormattedNovelToken WHO = new FormattedNovelToken.Builder()
            .id("18")
            .url("mock://formatted_novel_tokens/18")
            .restVerb(RestVerb.GET)
            .content("who")
            .ordinal(1)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(34))
            .build();

    static final FormattedNovelToken NOW = new FormattedNovelToken.Builder()
            .id("17")
            .url("mock://formatted_novel_tokens/17")
            .restVerb(RestVerb.GET)
            .content("Now,")
            .ordinal(0)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(35))
            .build();

    static final FormattedNovelToken LEAVE = new FormattedNovelToken.Builder()
            .id("16")
            .url("mock://formatted_novel_tokens/16")
            .restVerb(RestVerb.GET)
            .content("leave!")
            .ordinal(1)
            .chapter("mock://chapters/9")
            .createdAt(new DateTime().minusHours(5))
            .build();

    static final FormattedNovelToken PLEASE2 = new FormattedNovelToken.Builder()
            .id("15")
            .url("mock://formatted_novel_tokens/15")
            .restVerb(RestVerb.GET)
            .content("Please")
            .ordinal(0)
            .chapter("mock://chapters/9")
            .createdAt(new DateTime().minusHours(5).minusMinutes(3))
            .build();

    static final FormattedNovelToken YOU = new FormattedNovelToken.Builder()
            .id("14")
            .url("mock://formatted_novel_tokens/14")
            .restVerb(RestVerb.GET)
            .content("you.")
            .ordinal(2)
            .chapter("mock://chapters/8")
            .createdAt(new DateTime().minusDays(1).minusMinutes(5))
            .build();

    static final FormattedNovelToken LOVE = new FormattedNovelToken.Builder()
            .id("13")
            .url("mock://formatted_novel_tokens/13")
            .restVerb(RestVerb.GET)
            .content("love")
            .ordinal(1)
            .chapter("mock://chapters/8")
            .createdAt(new DateTime().minusDays(1).minusMinutes(6))
            .build();

    static final FormattedNovelToken I = new FormattedNovelToken.Builder()
            .id("12")
            .url("mock://formatted_novel_tokens/12")
            .restVerb(RestVerb.GET)
            .content("I")
            .ordinal(0)
            .chapter("mock://chapters/8")
            .createdAt(new DateTime().minusDays(1).minusMinutes(8))
            .build();

    static final FormattedNovelToken FACE = new FormattedNovelToken.Builder()
            .id("11")
            .url("mock://formatted_novel_tokens/11")
            .restVerb(RestVerb.GET)
            .content("face!")
            .ordinal(0)
            .chapter("mock://chapters/7")
            .createdAt(new DateTime().minusDays(2).minusHours(1))
            .build();

    static final FormattedNovelToken MY = new FormattedNovelToken.Builder()
            .id("10")
            .url("mock://formatted_novel_tokens/10")
            .restVerb(RestVerb.GET)
            .content("my")
            .ordinal(0)
            .chapter("mock://chapters/6")
            .createdAt(new DateTime().minusDays(2).minusHours(4))
            .build();

    static final FormattedNovelToken EAT = new FormattedNovelToken.Builder()
            .id("9")
            .url("mock://formatted_novel_tokens/9")
            .restVerb(RestVerb.GET)
            .content("Eat")
            .ordinal(0)
            .chapter("mock://chapters/5")
            .createdAt(new DateTime().minusDays(2).minusHours(9))
            .build();

    static final FormattedNovelToken PURITY = new FormattedNovelToken.Builder()
            .id("8")
            .url("mock://formatted_novel_tokens/8")
            .restVerb(RestVerb.GET)
            .content("purity.\"")
            .ordinal(4)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(30))
            .build();

    static final FormattedNovelToken YOUR = new FormattedNovelToken.Builder()
            .id("7")
            .url("mock://formatted_novel_tokens/7")
            .restVerb(RestVerb.GET)
            .content("your")
            .ordinal(3)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(32))
            .build();

    static final FormattedNovelToken SACRIFICE = new FormattedNovelToken.Builder()
            .id("6")
            .url("mock://formatted_novel_tokens/6")
            .restVerb(RestVerb.GET)
            .content("sacrifice")
            .ordinal(2)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(33))
            .build();

    static final FormattedNovelToken DONT = new FormattedNovelToken.Builder()
            .id("5")
            .url("mock://formatted_novel_tokens/5")
            .restVerb(RestVerb.GET)
            .content("Don't")
            .ordinal(1)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(34))
            .build();

    static final FormattedNovelToken STOP = new FormattedNovelToken.Builder()
            .id("4")
            .url("mock://formatted_novel_tokens/4")
            .restVerb(RestVerb.GET)
            .content("\"STOP!")
            .ordinal(0)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(35))
            .build();

    static final FormattedNovelToken AWAY = new FormattedNovelToken.Builder()
            .id("3")
            .url("mock://formatted_novel_tokens/3")
            .restVerb(RestVerb.GET)
            .content("Away.")
            .ordinal(0)
            .chapter("mock://chapters/3")
            .createdAt(new DateTime().minusDays(7).minusHours(2))
            .build();

    static final FormattedNovelToken GO = new FormattedNovelToken.Builder()
            .id("2")
            .url("mock://formatted_novel_tokens/2")
            .restVerb(RestVerb.GET)
            .content("Go.")
            .ordinal(0)
            .chapter("mock://chapters/2")
            .createdAt(new DateTime().minusDays(7).minusHours(4))
            .build();

    static final FormattedNovelToken PLEASE = new FormattedNovelToken.Builder()
            .id("1")
            .url("mock://formatted_novel_tokens/1")
            .restVerb(RestVerb.GET)
            .content("Please.")
            .ordinal(0)
            .chapter("mock://chapters/1")
            .createdAt(new DateTime().minusDays(7).minusHours(6))
            .build();

    private MockChapterText() {
        throw new AssertionError("No instances.");
    }
}

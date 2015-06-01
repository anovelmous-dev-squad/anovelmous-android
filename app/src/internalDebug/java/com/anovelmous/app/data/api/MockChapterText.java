package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.model.FormattedNovelToken;

import org.joda.time.DateTime;

/**
 * Created by Greg Ziegan on 6/2/15.
 */
final class MockChapterText {
    static final FormattedNovelToken DADDY = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/21")
            .content("daddy?")
            .ordinal(4)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(30))
            .build();

    static final FormattedNovelToken YOUR2 = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/20")
            .content("your")
            .ordinal(3)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(32))
            .build();

    static final FormattedNovelToken IS = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/19")
            .content("is")
            .ordinal(2)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(33))
            .build();

    static final FormattedNovelToken WHO = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/18")
            .content("who")
            .ordinal(1)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(34))
            .build();

    static final FormattedNovelToken NOW = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/17")
            .content("Now,")
            .ordinal(0)
            .chapter("mock://chapters/10")
            .createdAt(new DateTime().minusMinutes(35))
            .build();

    static final FormattedNovelToken LEAVE = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/16")
            .content("leave!")
            .ordinal(1)
            .chapter("mock://chapters/9")
            .createdAt(new DateTime().minusHours(5))
            .build();

    static final FormattedNovelToken PLEASE2 = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/15")
            .content("Please")
            .ordinal(0)
            .chapter("mock://chapters/9")
            .createdAt(new DateTime().minusHours(5).minusMinutes(3))
            .build();

    static final FormattedNovelToken YOU = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/14")
            .content("you.")
            .ordinal(2)
            .chapter("mock://chapters/8")
            .createdAt(new DateTime().minusDays(1).minusMinutes(5))
            .build();

    static final FormattedNovelToken LOVE = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/13")
            .content("love")
            .ordinal(1)
            .chapter("mock://chapters/8")
            .createdAt(new DateTime().minusDays(1).minusMinutes(6))
            .build();

    static final FormattedNovelToken I = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/12")
            .content("I")
            .ordinal(0)
            .chapter("mock://chapters/8")
            .createdAt(new DateTime().minusDays(1).minusMinutes(8))
            .build();

    static final FormattedNovelToken FACE = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/11")
            .content("face!")
            .ordinal(0)
            .chapter("mock://chapters/7")
            .createdAt(new DateTime().minusDays(2).minusHours(1))
            .build();

    static final FormattedNovelToken MY = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/10")
            .content("my")
            .ordinal(0)
            .chapter("mock://chapters/6")
            .createdAt(new DateTime().minusDays(2).minusHours(4))
            .build();

    static final FormattedNovelToken EAT = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/9")
            .content("Eat")
            .ordinal(0)
            .chapter("mock://chapters/5")
            .createdAt(new DateTime().minusDays(2).minusHours(9))
            .build();

    static final FormattedNovelToken PURITY = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/8")
            .content("purity.\"")
            .ordinal(4)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(30))
            .build();

    static final FormattedNovelToken YOUR = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/7")
            .content("your")
            .ordinal(3)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(32))
            .build();

    static final FormattedNovelToken SACRIFICE = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/6")
            .content("sacrifice")
            .ordinal(2)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(33))
            .build();

    static final FormattedNovelToken DONT = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/5")
            .content("Don't")
            .ordinal(1)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(34))
            .build();

    static final FormattedNovelToken STOP = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/4")
            .content("\"STOP!")
            .ordinal(0)
            .chapter("mock://chapters/4")
            .createdAt(new DateTime().minusDays(7).minusMinutes(35))
            .build();

    static final FormattedNovelToken AWAY = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/3")
            .content("Away.")
            .ordinal(0)
            .chapter("mock://chapters/3")
            .createdAt(new DateTime().minusDays(7).minusHours(2))
            .build();

    static final FormattedNovelToken GO = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/2")
            .content("Go.")
            .ordinal(0)
            .chapter("mock://chapters/2")
            .createdAt(new DateTime().minusDays(7).minusHours(4))
            .build();

    static final FormattedNovelToken PLEASE = new FormattedNovelToken.Builder()
            .url("mock://formatted_novel_tokens/1")
            .content("Please.")
            .ordinal(0)
            .chapter("mock://chapters/1")
            .createdAt(new DateTime().minusDays(7).minusHours(6))
            .build();
}

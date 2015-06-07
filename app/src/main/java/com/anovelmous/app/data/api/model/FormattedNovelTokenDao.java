package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.FormattedNovelToken;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class FormattedNovelTokenDao extends TimeStampedDao {
    private String content;
    private int ordinal;
    private String chapter;

    public FormattedNovelTokenDao(FormattedNovelToken formattedNovelToken) {
        id = formattedNovelToken.id;
        url = formattedNovelToken.url;
        content = formattedNovelToken.content;
        ordinal = formattedNovelToken.ordinal;
        chapter = formattedNovelToken.chapter;
        createdAt = formattedNovelToken.createdAt.toDate();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}

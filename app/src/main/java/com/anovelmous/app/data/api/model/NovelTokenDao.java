package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.NovelToken;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class NovelTokenDao extends TimeStampedDao {
    private String token;
    private int ordinal;
    private String chapter;

    public NovelTokenDao() {

    }

    public NovelTokenDao(NovelToken novelToken) {
        id = novelToken.id;
        url = novelToken.url;
        token = novelToken.token;
        ordinal = novelToken.ordinal;
        chapter = novelToken.chapter;
        createdAt = novelToken.createdAt.toDate();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.FormattedNovelToken;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class RealmFormattedNovelToken extends RealmObject {
    private long id;
    @PrimaryKey private String url;
    private String content;
    private int ordinal;
    private RealmChapter chapter;
    private Date createdAt;

    public RealmFormattedNovelToken() {

    }

    public RealmFormattedNovelToken(FormattedNovelToken formattedNovelToken, Realm realm) {
        id = formattedNovelToken.id;
        url = formattedNovelToken.url;
        content = formattedNovelToken.content;
        ordinal = formattedNovelToken.ordinal;
        chapter = realm.where(RealmChapter.class)
                .equalTo("url", formattedNovelToken.chapter).findFirst();
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

    public RealmChapter getChapter() {
        return chapter;
    }

    public void setChapter(RealmChapter chapter) {
        this.chapter = chapter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

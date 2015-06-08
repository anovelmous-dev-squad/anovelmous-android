package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.NovelToken;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class NovelTokenDao extends RealmObject {
    private long id;
    @PrimaryKey private String url;
    private String token;
    private int ordinal;
    private ChapterDao chapter;
    private Date createdAt;

    public NovelTokenDao() {

    }

    public NovelTokenDao(NovelToken novelToken, Realm realm) {
        id = novelToken.id;
        url = novelToken.url;
        token = novelToken.token;
        ordinal = novelToken.ordinal;
        chapter = realm.where(ChapterDao.class).equalTo("url", novelToken.chapter).findFirst();
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

    public ChapterDao getChapter() {
        return chapter;
    }

    public void setChapter(ChapterDao chapter) {
        this.chapter = chapter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Vote;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Greg Ziegan on 6/13/15.
 */
public class RealmVote extends RealmObject {
    @PrimaryKey private long id;
    private String url;
    private RealmToken token;
    private int ordinal;
    private boolean selected;
    private RealmChapter chapter;
    private RealmUser user;
    private Date createdAt;

    public RealmVote() {

    }

    public RealmVote(Vote vote, Realm realm) {
        id = vote.id;
        url = vote.url;
        token = realm.where(RealmToken.class).equalTo("url", vote.token).findFirst();
        ordinal = vote.ordinal;
        selected = vote.selected;
        chapter = realm.where(RealmChapter.class).equalTo("url", vote.chapter).findFirst();
        user = realm.where(RealmUser.class).equalTo("url", vote.user).findFirst();
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

    public RealmToken getToken() {
        return token;
    }

    public void setToken(RealmToken token) {
        this.token = token;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public RealmChapter getChapter() {
        return chapter;
    }

    public void setChapter(RealmChapter chapter) {
        this.chapter = chapter;
    }

    public RealmUser getUser() {
        return user;
    }

    public void setUser(RealmUser user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Vote;

import java.util.Date;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Greg Ziegan on 6/13/15.
 */
public class RealmVote extends RealmObject {
    @PrimaryKey private String id;
    private String url;
    private RealmToken token;
    private int ordinal;
    private boolean selected;
    private RealmChapter chapter;
    private RealmContributor user;
    private Date createdAt;

    private String restVerb;
    private boolean lastRequestFinished;

    public RealmVote() {

    }

    public RealmVote(Vote vote, Realm realm) {
        id = (vote.id == null) ? UUID.randomUUID().toString() : vote.id;
        url = vote.url;
        token = realm.where(RealmToken.class).equalTo("url", vote.token).findFirst();
        ordinal = vote.ordinal;
        selected = vote.selected;
        chapter = realm.where(RealmChapter.class).equalTo("url", vote.chapter).findFirst();
        user = realm.where(RealmContributor.class).equalTo("url", vote.user).findFirst();
        restVerb = vote.restVerb.toString();
        lastRequestFinished = vote.restVerb.equals(RestVerb.GET); // Does not matter if a GET finishes
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public RealmContributor getUser() {
        return user;
    }

    public void setUser(RealmContributor user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRestVerb() {
        return restVerb;
    }

    public void setRestVerb(String restVerb) {
        this.restVerb = restVerb;
    }

    public boolean isLastRequestFinished() {
        return lastRequestFinished;
    }

    public void setLastRequestFinished(boolean lastRequestFinished) {
        this.lastRequestFinished = lastRequestFinished;
    }
}

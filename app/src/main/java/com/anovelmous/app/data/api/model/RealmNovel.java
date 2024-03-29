package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Novel;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class RealmNovel extends RealmObject {
    @PrimaryKey private String id;
    private String url;
    private String title;
    private boolean isCompleted;
    private int votingDuration;
    private Date createdAt;

    public RealmNovel() {

    }

    public RealmNovel(Novel novel) {
        id = (novel.id == null) ? UUID.randomUUID().toString() : novel.id;
        id = novel.id;
        url = novel.url;
        title = novel.title;
        isCompleted = novel.isCompleted;
        votingDuration = novel.votingDuration;
        createdAt = novel.createdAt.toDate();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public int getVotingDuration() {
        return votingDuration;
    }

    public void setVotingDuration(int votingDuration) {
        this.votingDuration = votingDuration;
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

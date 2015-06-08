package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Chapter;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class ChapterDao extends RealmObject {
    private long id;
    @PrimaryKey private String url;
    private String title;
    private boolean isCompleted;
    private int votingDuration;
    private NovelDao novel;
    private Date createdAt;

    public ChapterDao() {

    }

    public ChapterDao(Chapter chapter, Realm realm) {
        id = chapter.id;
        url = chapter.url;
        title = chapter.title;
        isCompleted = chapter.isCompleted;
        votingDuration = chapter.votingDuration;
        novel = realm.where(NovelDao.class).equalTo("url", chapter.novel).findFirst();
        createdAt = chapter.createdAt.toDate();
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

    public NovelDao getNovel() {
        return novel;
    }

    public void setNovel(NovelDao novel) {
        this.novel = novel;
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

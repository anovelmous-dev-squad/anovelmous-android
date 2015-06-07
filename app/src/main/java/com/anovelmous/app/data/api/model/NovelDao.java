package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Novel;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class NovelDao extends TimeStampedDao {
    private String title;
    private boolean isCompleted;
    private int votingDuration;

    public NovelDao() {

    }

    public NovelDao(Novel novel) {
        id = novel.id;
        url = novel.url;
        title = novel.title;
        isCompleted = novel.isCompleted;
        votingDuration = novel.votingDuration;
        createdAt = novel.createdAt.toDate();
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

}

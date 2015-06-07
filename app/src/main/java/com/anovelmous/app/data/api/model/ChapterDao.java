package com.anovelmous.app.data.api.model;

import com.anovelmous.app.data.api.resource.Chapter;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class ChapterDao extends TimeStampedDao {
    private String title;
    private boolean isCompleted;
    private int votingDuration;
    private String novel;

    public ChapterDao() {

    }

    public ChapterDao(Chapter chapter) {
        title = chapter.title;
        isCompleted = chapter.isCompleted;
        votingDuration = chapter.votingDuration;
        novel = chapter.novel;
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

    public String getNovel() {
        return novel;
    }

    public void setNovel(String novel) {
        this.novel = novel;
    }
}

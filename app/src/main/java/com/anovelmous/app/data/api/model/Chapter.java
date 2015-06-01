package com.anovelmous.app.data.api.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 6/2/15.
 */
public class Chapter {
    @NonNull public final String url;

    @NonNull public final String title;

    @SerializedName("is_completed")
    public final boolean isCompleted;

    @SerializedName("voting_duration")
    public final int votingDuration;

    @NonNull public final String novel;

    @NonNull public final DateTime createdAt;

    private Chapter(Builder builder) {
        this.url = checkNotNull(builder.url, "url == null");
        this.title = checkNotNull(builder.title, "title == null");
        this.isCompleted = checkNotNull(builder.isCompleted, "isCompleted == null");
        this.votingDuration = checkNotNull(builder.votingDuration, "votingDuration == null");
        this.novel = checkNotNull(builder.novel, "novel == null");
        this.createdAt = checkNotNull(builder.createdAt, "createdAt == null");
    }

    public static final class Builder {
        private String url;
        private String title;
        private boolean isCompleted;
        private int votingDuration;
        private String novel;
        private DateTime createdAt;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder isCompleted(boolean isCompleted) {
            this.isCompleted = isCompleted;
            return this;
        }

        public Builder votingDuration(int votingDuration) {
            this.votingDuration = votingDuration;
            return this;
        }

        public Builder novel(String novel) {
            this.novel = novel;
            return this;
        }

        public Builder createdAt(DateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Chapter build() {
            return new Chapter(this);
        }
    }

}

package com.anovelmous.app.data.api.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 5/31/15.
 */
public class Novel {
    @NonNull public final String title;

    @SerializedName("is_completed")
    public final boolean isCompleted;

    @SerializedName("voting_duration")
    public final int votingDuration;

    @SerializedName("created_at")
    @NonNull public final DateTime createdAt;

    private Novel(Builder builder) {
        this.title = checkNotNull(builder.title, "title == null");
        this.isCompleted = builder.isCompleted;
        this.votingDuration = builder.votingDuration;
        this.createdAt = checkNotNull(builder.createdAt, "createdAt == null");
    }

    @Override
    public String toString() {
        return "Novel: " + title;
    }

    public static final class Builder {
        private String title;
        private boolean isCompleted;
        private int votingDuration;
        private DateTime createdAt;

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

        public Builder createdAt(DateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Novel build() {
            return new Novel(this);
        }
    }
}

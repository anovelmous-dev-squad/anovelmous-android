package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 6/2/15.
 */
public class Chapter extends TimeStampedResource {
    @NonNull public final String title;

    @SerializedName("is_completed")
    public final boolean isCompleted;

    @SerializedName("voting_duration")
    public final int votingDuration;

    @NonNull public final String novel;

    private Chapter(Builder builder) {
        super(builder);
        this.title = checkNotNull(builder.title, "title == null");
        this.isCompleted = checkNotNull(builder.isCompleted, "isCompleted == null");
        this.votingDuration = checkNotNull(builder.votingDuration, "votingDuration == null");
        this.novel = checkNotNull(builder.novel, "novel == null");
    }

    public static final class Builder extends TimeStampedResource.Builder<Builder> {
        private String title;
        private boolean isCompleted;
        private int votingDuration;
        private String novel;

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

        public Chapter build() {
            return new Chapter(this);
        }
    }

    @Override
    public String toString() {
        return "<Chapter: " + id + " title: " + title + ">";
    }
}

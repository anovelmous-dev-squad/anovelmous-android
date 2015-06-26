package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.RestVerb;
import com.google.gson.annotations.SerializedName;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 5/31/15.
 */
public class Novel extends TimeStampedResource {
    @NonNull public final String title;

    @SerializedName("is_completed")
    public final boolean isCompleted;

    @SerializedName("voting_duration")
    public final int votingDuration;

    private Novel(Builder builder) {
        super(builder);
        this.title = checkNotNull(builder.title, "title == null");
        this.isCompleted = builder.isCompleted;
        this.votingDuration = builder.votingDuration;
    }

    @Override
    public String toString() {
        return "<Novel: " + title + ">";
    }

    public static final class Builder extends TimeStampedResource.Builder<Builder> {
        private String title;
        private boolean isCompleted;
        private int votingDuration;


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

        public Novel build() {
            return new Novel(this);
        }
    }
}

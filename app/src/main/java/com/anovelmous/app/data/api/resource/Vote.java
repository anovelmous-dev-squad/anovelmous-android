package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class Vote extends TimeStampedResource {
    @NonNull public final String token;
    public final int ordinal;
    public final boolean selected;
    @NonNull public final String chapter;
    @NonNull public final String user;

    private Vote(Builder builder) {
        super(builder);
        this.token = checkNotNull(builder.token, "token == null");
        this.ordinal = builder.ordinal;
        this.selected = builder.selected;
        this.chapter = checkNotNull(builder.chapter, "chapter == null");
        this.user = checkNotNull(builder.user, "user == null");
    }

    public static final class Builder extends TimeStampedResource.Builder<Builder> {
        private String token;
        private int ordinal;
        private boolean selected;
        private String chapter;
        private String user;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder ordinal(int ordinal) {
            this.ordinal = ordinal;
            return this;
        }

        public Builder selected(boolean selected) {
            this.selected = selected;
            return this;
        }

        public Builder chapter(String chapter) {
            this.chapter = chapter;
            return this;

        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Vote build() {
            return new Vote(this);
        }
    }
}

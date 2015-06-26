package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.RestVerb;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class NovelToken extends TimeStampedResource {
    @NonNull public final String token;
    public final int ordinal;
    @NonNull public final String chapter;

    public NovelToken(Builder builder) {
        super(builder);
        this.token = checkNotNull(builder.token, "token == null");
        this.ordinal = builder.ordinal;
        this.chapter = checkNotNull(builder.chapter, "chapter == null");
    }

    @Override
    public String toString() {
        return "<NovelToken: " + url + ">";
    }

    public static final class Builder extends TimeStampedResource.Builder<Builder> {
        private String token;
        private int ordinal;
        private String chapter;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder ordinal(int ordinal) {
            this.ordinal = ordinal;
            return this;
        }

        public Builder chapter(String chapter) {
            this.chapter = chapter;
            return this;
        }

        public NovelToken build() {
            return new NovelToken(this);
        }
    }
}

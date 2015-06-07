package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class NovelToken {
    public final long id;
    @NonNull public final String url;

    @NonNull public final String token;

    public final int ordinal;

    @NonNull public final String chapter;

    @SerializedName("created_at")
    @NonNull public final DateTime createdAt;

    public NovelToken(Builder builder) {
        this.id = builder.id;
        this.url = checkNotNull(builder.url, "url == null");
        this.token = checkNotNull(builder.token, "token == null");
        this.ordinal = builder.ordinal;
        this.chapter = checkNotNull(builder.chapter, "chapter == null");
        this.createdAt = checkNotNull(builder.createdAt, "createdAt == null");
    }

    @Override
    public String toString() {
        return "<NovelToken: " + url + ">";
    }

    public static final class Builder {
        private long id;
        private String url;
        private String token;
        private int ordinal;
        private String chapter;
        private DateTime createdAt;
        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

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

        public Builder createdAt(DateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NovelToken build() {
            return new NovelToken(this);
        }
    }
}

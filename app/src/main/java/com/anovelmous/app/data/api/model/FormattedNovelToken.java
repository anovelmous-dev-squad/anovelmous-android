package com.anovelmous.app.data.api.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 6/2/15.
 */
public class FormattedNovelToken {
    public final long id;
    @NonNull public final String url;

    @NonNull public final String content;

    public final int ordinal;

    @NonNull public final String chapter;

    @SerializedName("created_at")
    @NonNull public final DateTime createdAt;

    public FormattedNovelToken(Builder builder) {
        this.id = builder.id;
        this.url = checkNotNull(builder.url, "url == null");
        this.content = checkNotNull(builder.content, "content == null");
        this.ordinal = builder.ordinal;
        this.chapter = checkNotNull(builder.chapter, "chapter == null");
        this.createdAt = checkNotNull(builder.createdAt, "createdAt == null");
    }

    public static final class Builder {
        private long id;
        private String url;
        private String content;
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

        public Builder content(String content) {
            this.content = content;
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

        public FormattedNovelToken build() {
            return new FormattedNovelToken(this);
        }
    }
}

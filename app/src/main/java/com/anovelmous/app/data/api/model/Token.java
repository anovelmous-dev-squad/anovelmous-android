package com.anovelmous.app.data.api.model;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class Token {
    public long id;
    @NonNull public String url;
    @NonNull public String content;
    public boolean isValid;
    public boolean isPunctuation;
    @NonNull public DateTime createdAt;

    private Token(Builder builder) {
        this.id = builder.id;
        this.url = checkNotNull(builder.url, "url == null");
        this.content = checkNotNull(builder.content, "content == null");
        this.isValid = builder.isValid;
        this.isPunctuation = builder.isPunctuation;
        this.createdAt = checkNotNull(builder.createdAt, "createdAt == null");
    }

    public static final class Builder {
        private long id;
        private String url;
        private String content;
        private boolean isValid;
        private boolean isPunctuation;
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

        public Builder isValid(boolean isValid) {
            this.isValid = isValid;
            return this;
        }

        public Builder isPunctuation(boolean isPunctuation) {
            this.isPunctuation = isPunctuation;
            return this;
        }

        public Builder createdAt(DateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Token build() {
            return new Token(this);
        }
    }
}

package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class Vote {
    public long id;

    @NonNull public String url;
    @NonNull public String token;
    public int ordinal;
    public boolean selected;
    @NonNull public String chapter;
    @NonNull public String user;

    @SerializedName("created_at")
    @NonNull public DateTime createdAt;

    private Vote(Builder builder) {
        this.id = builder.id;
        this.url = checkNotNull(builder.url, "url == null");
        this.token = checkNotNull(builder.token, "token == null");
        this.ordinal = builder.ordinal;
        this.selected = builder.selected;
        this.chapter = checkNotNull(builder.chapter, "chapter == null");
        this.user = checkNotNull(builder.user, "user == null");
        this.createdAt = checkNotNull(builder.createdAt, "createdAt == null");
    }

    public static final class Builder {
        private long id;
        private String url;
        private String token;
        private int ordinal;
        private boolean selected;
        private String chapter;
        private String user;
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

        public Builder createdAt(DateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Vote build() {
            return new Vote(this);
        }
    }
}

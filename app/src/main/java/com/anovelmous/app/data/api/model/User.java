package com.anovelmous.app.data.api.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class User {
    public long id;
    @NonNull public String url;
    @NonNull public String username;
    @NonNull public String email;
    @NonNull public List<String> groups;

    @SerializedName("date_joined")
    @NonNull public DateTime dateJoined;

    private User(Builder builder) {
        this.id = builder.id;
        this.url = checkNotNull(builder.url, "url == null");
        this.username = checkNotNull(builder.username, "username == null");
        this.email = checkNotNull(builder.email, "email == null");
        this.groups = checkNotNull(builder.groups, "groups == null");
        this.dateJoined = checkNotNull(builder.dateJoined, "dateJoined == null");
    }

    private static final class Builder {
        private long id;
        private String url;
        private String username;
        private String email;
        private List<String> groups;
        private DateTime dateJoined;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder groups(ArrayList<String> groups) {
            this.groups = groups;
            return this;
        }

        public Builder dateJoined(DateTime dateJoined) {
            this.dateJoined = dateJoined;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}

package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.RestVerb;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import java.util.List;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class User extends BaseResource {
    @NonNull public final String username;
    @NonNull public final String email;
    @NonNull public final List<String> groups;

    @SerializedName("date_joined")
    @NonNull public DateTime dateJoined;

    private User(Builder builder) {
        super(builder);
        this.username = checkNotNull(builder.username, "username == null");
        this.email = checkNotNull(builder.email, "email == null");
        this.groups = checkNotNull(builder.groups, "groups == null");
        if (builder.restVerb == RestVerb.GET)
            this.dateJoined = checkNotNull(builder.dateJoined, "dateJoined == null");
        else
            this.dateJoined = new DateTime();
    }

    public static final class Builder extends BaseResource.Builder<Builder> {
        private String username;
        private String email;
        private List<String> groups;
        private DateTime dateJoined;

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder groups(List<String> groups) {
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

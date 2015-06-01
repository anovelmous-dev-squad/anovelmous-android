package com.anovelmous.app.data.api.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 5/31/15.
 */
public class Novel {
    @NonNull public final String name;
    @SerializedName("updated_at")
    public final DateTime updatedAt;

    private Novel(Builder builder) {
        this.name = checkNotNull(builder.name, "name == null");
        this.updatedAt = checkNotNull(builder.updatedAt, "updatedAt == null");
    }

    @Override
    public String toString() {
        return "Novel: " + name;
    }

    public static final class Builder {
        private String name;
        private DateTime updatedAt;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder updatedAt(DateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Novel build() {
            return new Novel(this);
        }
    }
}

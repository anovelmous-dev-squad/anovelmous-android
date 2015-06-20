package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class Group extends BaseResource {
    @NonNull public final String name;

    public Group(Builder builder) {
        super(builder);
        this.name = checkNotNull(builder.name, "name == null");
    }

    private static final class Builder extends BaseResource.Builder<Builder> {
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Group build() {
            return new Group(this);
        }
    }
}

package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
public class Guild extends BaseResource {
    @NonNull public final String name;

    public Guild(Builder builder) {
        super(builder);
        this.name = checkNotNull(builder.name, "name == null");
    }

    public static final class Builder extends BaseResource.Builder<Builder> {
        private String name;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Guild build() {
            return new Guild(this);
        }
    }
}

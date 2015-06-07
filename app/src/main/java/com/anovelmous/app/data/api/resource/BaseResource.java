package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;
import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
abstract class BaseResource {
    public final long id;
    @NonNull public final String url;

    public BaseResource(Builder builder) {
        id = builder.id;
        url = checkNotNull(builder.url, "url == null");
    }

    protected static class Builder {
        protected long id;
        protected String url;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }
    }

}

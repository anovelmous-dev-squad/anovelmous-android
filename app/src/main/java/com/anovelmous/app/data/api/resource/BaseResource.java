package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.RestVerb;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
abstract class BaseResource {
    public final long id;
    @NonNull public final String url;
    @NonNull public final RestVerb restVerb;

    protected BaseResource(Builder builder) {
        restVerb = checkNotNull(builder.restVerb, "restVerb == null");

        if (restVerb == RestVerb.POST) {
            id = -1;
            url = "";
        } else {
            id = builder.id;
            url = checkNotNull(builder.url, "url == null");
        }
    }

    protected abstract static class Builder<T extends Builder>{
        protected long id;
        protected String url;
        protected RestVerb restVerb;

        public T id(long id) {
            this.id = id;
            return (T) this;
        }

        public T url(String url) {
            this.url = url;
            return (T) this;
        }

        public T restVerb(RestVerb restVerb) {
            this.restVerb = restVerb;
            return (T) this;
        }
    }

}

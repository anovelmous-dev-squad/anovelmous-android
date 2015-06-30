package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.RestVerb;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
abstract class BaseResource {
    public final String id;
    public final String url;
    @NonNull public final RestVerb restVerb;

    protected BaseResource(Builder builder) {
        restVerb = checkNotNull(builder.restVerb, "restVerb == null");
        id = builder.id;
        url = builder.url;
    }

    protected abstract static class Builder<T extends Builder>{
        protected String id;
        protected String url;
        protected RestVerb restVerb;

        public T id(String id) {
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

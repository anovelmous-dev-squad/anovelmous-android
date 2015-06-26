package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.RestVerb;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
abstract class TimeStampedResource extends BaseResource {
    @NonNull public final DateTime createdAt;

    protected TimeStampedResource(Builder builder) {
        super(builder);
        if (restVerb == RestVerb.POST)
            createdAt = new DateTime();
        else
            createdAt = checkNotNull(builder.createdAt, "createdAt == null");
    }

    protected abstract static class Builder<T extends Builder> extends BaseResource.Builder<T> {
        protected DateTime createdAt;

        public T createdAt(DateTime createdAt) {
            this.createdAt = createdAt;
            return (T) this;
        }
    }
}

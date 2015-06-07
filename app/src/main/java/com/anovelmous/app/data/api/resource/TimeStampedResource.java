package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by IntelliJ
 * Author: Greg Ziegan on 6/7/15.
 */
abstract class TimeStampedResource extends BaseResource {
    @NonNull public DateTime createdAt;

    public TimeStampedResource(Builder builder) {
        super(builder);
        createdAt = checkNotNull(builder.createdAt, "createdAt == null");
    }

    protected static class Builder extends BaseResource.Builder {
        protected DateTime createdAt;

        public Builder createdAt(DateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
    }
}

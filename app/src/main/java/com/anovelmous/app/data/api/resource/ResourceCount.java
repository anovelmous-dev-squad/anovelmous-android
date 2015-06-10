package com.anovelmous.app.data.api.resource;

/**
 * Created by Greg Ziegan on 6/9/15.
 */
public final class ResourceCount {
    public final int count;

    private ResourceCount(Builder builder) {
        count = builder.count;
    }

    public static final class Builder {
        private int count;

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public ResourceCount build() {
            return new ResourceCount(this);
        }
    }
}

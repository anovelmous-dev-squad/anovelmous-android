package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import static com.anovelmous.app.util.Preconditions.checkNotNull;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class Token extends TimeStampedResource {
    @NonNull public final String content;
    public final boolean isValid;
    public final boolean isPunctuation;

    private Token(Builder builder) {
        super(builder);
        this.content = checkNotNull(builder.content, "content == null");
        this.isValid = builder.isValid;
        this.isPunctuation = builder.isPunctuation;
    }

    public static final class Builder extends TimeStampedResource.Builder {
        private String content;
        private boolean isValid;
        private boolean isPunctuation;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder isValid(boolean isValid) {
            this.isValid = isValid;
            return this;
        }

        public Builder isPunctuation(boolean isPunctuation) {
            this.isPunctuation = isPunctuation;
            return this;
        }

        public Token build() {
            return new Token(this);
        }
    }
}

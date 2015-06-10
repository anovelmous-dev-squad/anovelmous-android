package com.anovelmous.app.data.api.resource;

import android.support.annotation.NonNull;

import static com.anovelmous.app.util.Preconditions.checkNotNull;
/**
 * Created by Greg Ziegan on 6/2/15.
 */
public class FormattedNovelToken extends TimeStampedResource {
    @NonNull public final String content;
    public final int ordinal;
    @NonNull public final String chapter;

    public FormattedNovelToken(Builder builder) {
        super(builder);
        this.content = checkNotNull(builder.content, "content == null");
        this.ordinal = builder.ordinal;
        this.chapter = checkNotNull(builder.chapter, "chapter == null");
    }

    @Override
    public String toString() {
        return content;
    }

    public static final class Builder extends TimeStampedResource.Builder<Builder> {
        private String content;
        private int ordinal;
        private String chapter;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder ordinal(int ordinal) {
            this.ordinal = ordinal;
            return this;
        }

        public Builder chapter(String chapter) {
            this.chapter = chapter;
            return this;
        }

        public FormattedNovelToken build() {
            return new FormattedNovelToken(this);
        }
    }
}

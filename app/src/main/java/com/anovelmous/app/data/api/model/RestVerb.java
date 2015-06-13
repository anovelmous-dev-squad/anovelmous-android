package com.anovelmous.app.data.api.model;

/**
 * Created by Greg Ziegan on 6/13/15.
 */
public enum RestVerb {
    GET("get"),
    POST("post"),
    PUT("put"),
    DELETE("delete");

    final String value;

    RestVerb(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

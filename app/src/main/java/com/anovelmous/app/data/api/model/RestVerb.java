package com.anovelmous.app.data.api.model;

/**
 * Created by Greg Ziegan on 6/13/15.
 */
public enum RestVerb {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    final String value;

    RestVerb(String value) {
        this.value = value;
    }

    public static RestVerb getValueForString(String s) {
        for (RestVerb v : values()) {
            if (s.equals(v.toString()))
                return v;
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
    }
}

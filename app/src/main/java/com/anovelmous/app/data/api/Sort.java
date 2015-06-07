package com.anovelmous.app.data.api;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public enum Sort {
    ID("id"),
    CREATED("created"),
    CREATED_AT("updated");

    private final String value;

    Sort(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return value;
    }
}


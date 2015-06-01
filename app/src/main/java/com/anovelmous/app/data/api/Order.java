package com.anovelmous.app.data.api;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public enum Order {
    ASC("asc"),
    DESC("desc");

    private final String value;

    Order(String value) {
        this.value = value;
    }

    @Override public String toString() {
        return value;
    }
}

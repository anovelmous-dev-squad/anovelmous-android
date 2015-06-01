package com.anovelmous.app.data;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public interface Clock {
    long millis();
    long nanos();

    Clock REAL = new Clock() {
        @Override public long millis() {
            return System.currentTimeMillis();
        }

        @Override public long nanos() {
            return System.nanoTime();
        }
    };
}

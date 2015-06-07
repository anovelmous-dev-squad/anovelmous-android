package com.anovelmous.app.data.api;

import android.support.annotation.NonNull;


import java.util.Comparator;

import static com.anovelmous.app.data.api.Order.ASC;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
abstract class OrderComparator<T> implements Comparator<T> {

    private final Order order;

    protected OrderComparator(Order order) {
        this.order = order;
    }

    @Override public final int compare(@NonNull T lhs, @NonNull T rhs) {
        return order == ASC ? compareAsc(lhs, rhs) : -compareAsc(lhs, rhs);
    }

    protected abstract int compareAsc(@NonNull T lhs, @NonNull T rhs);
}

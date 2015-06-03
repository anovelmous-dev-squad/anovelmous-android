package com.anovelmous.app.data.api;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.Novel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.anovelmous.app.data.api.Order.ASC;
import static com.anovelmous.app.data.api.Order.DESC;
/**
 * Created by Greg Ziegan on 5/31/15.
 */
final class SortUtil {
    private static final createdAtComparator UPDATED_ASC = new createdAtComparator(ASC);
    private static final createdAtComparator UPDATED_DESC = new createdAtComparator(DESC);

    static void sort(List<Novel> novels, Sort sort, Order order) {
        if (novels == null) return;

        switch (sort) {
            case UPDATED:
                Collections.sort(novels, order == ASC ? UPDATED_ASC : UPDATED_DESC);
                break;
            default:
                throw new IllegalArgumentException("Unknown sort: " + sort);
        }
    }

    private static abstract class OrderComparator<T> implements Comparator<T> {
        private final Order order;

        protected OrderComparator(Order order) {
            this.order = order;
        }

        @Override public final int compare(@NonNull T lhs, @NonNull T rhs) {
            return order == ASC ? compareAsc(lhs, rhs) : -compareAsc(lhs, rhs);
        }

        protected abstract int compareAsc(@NonNull T lhs, @NonNull T rhs);
    }

    private static final class createdAtComparator extends OrderComparator<Novel> {
        protected createdAtComparator(Order order) {
            super(order);
        }

        @Override public int compareAsc(@NonNull Novel lhs, @NonNull Novel rhs) {
            return lhs.createdAt.compareTo(rhs.createdAt);
        }
    }

    private SortUtil() {
    }
}

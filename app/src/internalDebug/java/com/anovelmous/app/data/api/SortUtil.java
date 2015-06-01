package com.anovelmous.app.data.api;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.Sort;
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
    private static final UpdatedComparator UPDATED_ASC = new UpdatedComparator(ASC);
    private static final UpdatedComparator UPDATED_DESC = new UpdatedComparator(DESC);

    static void sort(List<Novel> repositories, Sort sort, Order order) {
        if (repositories == null) return;

        switch (sort) {
            case UPDATED:
                Collections.sort(repositories, order == ASC ? UPDATED_ASC : UPDATED_DESC);
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

    private static final class UpdatedComparator extends OrderComparator<Novel> {
        protected UpdatedComparator(Order order) {
            super(order);
        }

        @Override public int compareAsc(@NonNull Novel lhs, @NonNull Novel rhs) {
            return lhs.updatedAt.compareTo(rhs.updatedAt);
        }
    }

    private SortUtil() {
    }
}

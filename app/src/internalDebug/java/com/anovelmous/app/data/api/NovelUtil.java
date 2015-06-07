package com.anovelmous.app.data.api;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.Novel;

import java.util.Collections;
import java.util.List;

import static com.anovelmous.app.data.api.Order.ASC;
import static com.anovelmous.app.data.api.Order.DESC;
/**
 * Created by Greg Ziegan on 5/31/15.
 */
final class NovelUtil {
    private static final createdAtComparator UPDATED_ASC = new createdAtComparator(ASC);
    private static final createdAtComparator UPDATED_DESC = new createdAtComparator(DESC);

    static void sort(List<Novel> novels, Sort sort, Order order) {
        if (novels == null) return;

        switch (sort) {
            case CREATED_AT:
                Collections.sort(novels, order == ASC ? UPDATED_ASC : UPDATED_DESC);
                break;
            default:
                throw new IllegalArgumentException("Unknown sort: " + sort);
        }
    }

    private static final class createdAtComparator extends OrderComparator<Novel> {
        protected createdAtComparator(Order order) {
            super(order);
        }

        @Override public int compareAsc(@NonNull Novel lhs, @NonNull Novel rhs) {
            return lhs.createdAt.compareTo(rhs.createdAt);
        }
    }

    private NovelUtil() {
    }
}

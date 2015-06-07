package com.anovelmous.app.data.api;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.Order;
import com.anovelmous.app.data.api.OrderComparator;
import com.anovelmous.app.data.api.Sort;
import com.anovelmous.app.data.api.model.FormattedNovelToken;

import java.util.Collections;
import java.util.List;

import static com.anovelmous.app.data.api.Order.ASC;
import static com.anovelmous.app.data.api.Order.DESC;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
final class FormattedNovelTokenUtil {
    private static final createdAtComparator UPDATED_ASC = new createdAtComparator(ASC);
    private static final createdAtComparator UPDATED_DESC = new createdAtComparator(DESC);

    static void sort(List<FormattedNovelToken> tokens, Sort sort, Order order) {
        if (tokens == null) return;

        switch (sort) {
            case CREATED_AT:
                Collections.sort(tokens, order == ASC ? UPDATED_ASC : UPDATED_DESC);
                break;
            default:
                throw new IllegalArgumentException("Unknown sort: " + sort);
        }
    }

    private static final class createdAtComparator extends OrderComparator<FormattedNovelToken> {
        protected createdAtComparator(Order order) {
            super(order);
        }

        @Override public int compareAsc(@NonNull FormattedNovelToken lhs, @NonNull FormattedNovelToken rhs) {
            return lhs.createdAt.compareTo(rhs.createdAt);
        }
    }

    private FormattedNovelTokenUtil() {
    }
}

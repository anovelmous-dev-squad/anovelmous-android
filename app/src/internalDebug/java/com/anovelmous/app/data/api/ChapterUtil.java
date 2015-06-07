package com.anovelmous.app.data.api;

import android.support.annotation.NonNull;

import com.anovelmous.app.data.api.model.Chapter;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.anovelmous.app.data.api.Order.ASC;
import static com.anovelmous.app.data.api.Order.DESC;

/**
 * Created by Greg Ziegan on 6/7/15.
 */
public class ChapterUtil {
    private static final createdAtComparator CREATED_AT_ASC = new createdAtComparator(ASC);
    private static final createdAtComparator CREATED_AT_DESC = new createdAtComparator(DESC);

    static void sort(List<Chapter> chapters, Sort sort, Order order) {
        if (chapters == null) return;

        switch (sort) {
            case CREATED_AT:
                Collections.sort(chapters, order == ASC ? CREATED_AT_ASC : CREATED_AT_DESC);
                break;
            default:
                throw new IllegalArgumentException("Unknown sort: " + sort);
        }
    }

    static void filter(List<Chapter> chapters, final String novel) {
        for (Iterator<Chapter> iterator = chapters.iterator(); iterator.hasNext();) {
            Chapter chapter = iterator.next();
            if (!chapter.novel.equals(novel))
                iterator.remove();
        }
    }

    private static final class createdAtComparator extends OrderComparator<Chapter> {
        protected createdAtComparator(Order order) {
            super(order);
        }

        @Override public int compareAsc(@NonNull Chapter lhs, @NonNull Chapter rhs) {
            return lhs.createdAt.compareTo(rhs.createdAt);
        }
    }

    private ChapterUtil() {
    }
}

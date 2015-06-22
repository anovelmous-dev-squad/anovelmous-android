package com.anovelmous.app.ui.contribute;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Greg Ziegan on 6/22/15.
 */
public class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable, Action1<List<String>> {
    private List<String> items = Collections.emptyList();

    public AutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void call(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public String getItem(int index) {
        return items.get(index);
    }
}

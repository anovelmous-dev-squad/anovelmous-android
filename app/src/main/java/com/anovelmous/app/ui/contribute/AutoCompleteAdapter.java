package com.anovelmous.app.ui.contribute;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Greg Ziegan on 6/22/15.
 */
public class AutoCompleteAdapter extends ArrayAdapter<String> implements Action1<List<String>> {
    private List<String> items = Collections.emptyList();
    final int LIMIT = 4;

    public AutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void call(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return Math.min(LIMIT, items.size());
    }

    @Override
    public String getItem(int index) {
        return items.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    List<String> filteredTokens = new ArrayList<>();
                    for(String token : items){
                        if(token.contains(constraint)){
                            filteredTokens.add(token);
                        }
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = filteredTokens;
                    filterResults.count = filteredTokens.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    items = (List<String>) results.values;
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

}

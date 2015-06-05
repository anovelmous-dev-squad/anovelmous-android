package com.anovelmous.app.ui.reading;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anovelmous.app.data.api.model.FormattedNovelToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Greg Ziegan on 6/4/15.
 */
public class ReadingAdapter extends ArrayAdapter<FormattedNovelToken>
    implements Action1<List<FormattedNovelToken>> {
    private List<FormattedNovelToken> formattedNovelTokens = Collections.emptyList();

    private static class ViewHolder {
        private TextView itemView;
    }

    public ReadingAdapter(Context context, int textViewResourceId, List<FormattedNovelToken> items) {
        super(context, textViewResourceId, items);
    }

    @Override
    public void call(List<FormattedNovelToken> formattedNovelTokens) {
        this.formattedNovelTokens = formattedNovelTokens;
        notifyDataSetChanged();
    }
}

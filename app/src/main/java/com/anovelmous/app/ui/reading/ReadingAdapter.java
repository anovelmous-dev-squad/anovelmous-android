package com.anovelmous.app.ui.reading;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anovelmous.app.data.api.model.FormattedNovelToken;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Greg Ziegan on 6/4/15.
 */
public class ReadingAdapter extends RecyclerView.Adapter<ReadingAdapter.ViewHolder>
    implements Action1<List<FormattedNovelToken>> {
    private List<FormattedNovelToken> formattedNovelTokens;
    private String chapterText;

    @Override
    public void call(List<FormattedNovelToken> formattedNovelTokens) {
        this.formattedNovelTokens = formattedNovelTokens;
        this.chapterText = "Lorem Ipsum"; // TODO transform chapter items
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindTo(chapterText);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return formattedNovelTokens.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView chapterTextView;

        public ViewHolder(TextView chapterTextView) {
            super(chapterTextView);
            this.chapterTextView = chapterTextView;
        }

        public void bindTo(String chapterText) {
            chapterTextView.setText(chapterText);
        }
    }
}

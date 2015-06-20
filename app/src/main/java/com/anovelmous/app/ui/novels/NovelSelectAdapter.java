package com.anovelmous.app.ui.novels;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.resource.Novel;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;
import timber.log.Timber;

final class NovelSelectAdapter extends RecyclerView.Adapter<NovelSelectAdapter.ViewHolder>
        implements Action1<List<Novel>> {
    public interface NovelClickListener {
        void onNovelClick(Novel novel);
    }

    private final NovelClickListener novelClickListener;
    private List<Novel> novels = Collections.emptyList();
    private final Context context;

    public NovelSelectAdapter(NovelClickListener novelClickListener, Context context) {
        this.novelClickListener = novelClickListener;
        this.context = context;
    }

    @Override public void call(final List<Novel> novels) {
        Timber.d("Novels received with size " + novels.size());
        this.novels = novels;
        notifyDataSetChanged();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        NovelItemView view = (NovelItemView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_item_novel, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bindTo(novels.get(i));
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public int getItemCount() {
        return novels.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        public final NovelItemView itemView;
        private Novel novel;

        public ViewHolder(NovelItemView itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    novelClickListener.onNovelClick(novel);
                }
            });
        }

        public void bindTo(Novel novel) {
            this.novel = novel;
            itemView.bindTo(novel);
        }
    }
}

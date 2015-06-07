package com.anovelmous.app.ui.chapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.resource.Chapter;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Greg Ziegan on 6/4/15.
 */
final class ChapterSelectAdapter extends RecyclerView.Adapter<ChapterSelectAdapter.ViewHolder>
    implements Action1<List<Chapter>> {

    public interface ChapterClickListener {
        void onChapterClick(Chapter chapter);
    }

    private final ChapterClickListener chapterClickListener;
    private List<Chapter> chapters = Collections.emptyList();

    public ChapterSelectAdapter(ChapterClickListener chapterClickListener) {
        this.chapterClickListener = chapterClickListener;
    }

    @Override
    public void call(List<Chapter> chapters) {
        this.chapters = chapters;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ChapterItemView view = (ChapterItemView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_item_chapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bindTo(chapters.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        public final ChapterItemView itemView;
        private Chapter chapter;

        public ViewHolder(ChapterItemView itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chapterClickListener.onChapterClick(chapter);
                }
            });
        }

        public void bindTo(Chapter chapter) {
            this.chapter = chapter;
            itemView.bindTo(chapter);
        }
    }
}

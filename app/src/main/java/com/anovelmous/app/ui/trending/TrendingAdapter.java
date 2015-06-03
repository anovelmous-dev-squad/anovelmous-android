package com.anovelmous.app.ui.trending;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.model.Chapter;
import com.anovelmous.app.data.api.model.Novel;

import java.util.Collections;
import java.util.List;

import rx.functions.Action1;

final class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder>
    implements Action1<List<Novel>> {
  public interface NovelClickListener {
    void onNovelClick(Novel novel);
  }

  private final NovelClickListener NovelClickListener;

  private List<Novel> repositories = Collections.emptyList();

  public TrendingAdapter(NovelClickListener NovelClickListener) {
    this.NovelClickListener = NovelClickListener;
  }

  @Override public void call(List<Novel> repositories) {
    this.repositories = repositories;
    notifyDataSetChanged();
  }

  @Override public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    TrendingItemView view = (TrendingItemView) LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.trending_view_novel, viewGroup, false);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(ViewHolder viewHolder, int i) {
    viewHolder.bindTo(repositories.get(i));
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemCount() {
    return repositories.size();
  }

  public final class ViewHolder extends RecyclerView.ViewHolder {
    public final TrendingItemView itemView;
    private Novel novel;

    public ViewHolder(TrendingItemView itemView) {
      super(itemView);
      this.itemView = itemView;
      this.itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          NovelClickListener.onNovelClick(novel);
        }
      });
    }

    public void bindTo(Novel novel) {
      this.novel = novel;
      itemView.bindTo(novel);
    }
  }
}

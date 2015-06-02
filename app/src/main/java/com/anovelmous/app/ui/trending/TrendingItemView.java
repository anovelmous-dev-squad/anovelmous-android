package com.anovelmous.app.ui.trending;

import android.content.Context;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.model.Novel;
import com.anovelmous.app.ui.misc.Truss;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public final class TrendingItemView extends RelativeLayout {
  @InjectView(R.id.trending_novel_name) TextView nameView;
  @InjectView(R.id.trending_novel_description) TextView descriptionView;

  private final int descriptionColor;

  public TrendingItemView(Context context, AttributeSet attrs) {
    super(context, attrs);

    TypedValue outValue = new TypedValue();
    context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, outValue, true);
    descriptionColor = outValue.data;
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(Novel novel) {
    nameView.setText(novel.title);

    Truss description = new Truss();

    description.pushSpan(new ForegroundColorSpan(descriptionColor));
    if (!novel.isCompleted) {
      description.append("Began Writing — ");
      DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM, yyyy");
      description.append(novel.createdAt.toString(fmt));
      description.popSpan();
    }

    descriptionView.setText(description.build());
  }
}

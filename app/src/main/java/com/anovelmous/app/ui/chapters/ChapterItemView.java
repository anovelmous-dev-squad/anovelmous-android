package com.anovelmous.app.ui.chapters;

import android.content.Context;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.model.Chapter;
import com.anovelmous.app.ui.misc.Truss;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Greg Ziegan on 6/4/15.
 */
public final class ChapterItemView extends RelativeLayout {

    @InjectView(R.id.chapter_item_name) TextView nameView;
    @InjectView(R.id.chapter_item_description) TextView descriptionView;

    private final int descriptionColor;

    public ChapterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, outValue, true);
        descriptionColor = outValue.data;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
    }

    public void bindTo(Chapter chapter) {
        nameView.setText(chapter.title);

        Truss description = new Truss();

        description.pushSpan(new ForegroundColorSpan(descriptionColor));
        if (!chapter.isCompleted) {
            description.append("Began Writing - ");
            DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMMM, yyyy");
            description.append(chapter.createdAt.toString(fmt));
        }
        description.popSpan();

        descriptionView.setText(description.build());
    }
}

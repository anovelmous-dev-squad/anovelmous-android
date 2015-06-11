package com.anovelmous.app.ui.reading;

import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import com.anovelmous.app.R;

/**
 * Created by Greg Ziegan on 6/11/15.
 */
public abstract class ReadingBaseActivity extends AppCompatActivity {
    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }
}

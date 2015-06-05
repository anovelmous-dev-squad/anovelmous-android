package com.anovelmous.app.ui.misc;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.NavUtils;
import android.view.View;

/**
 * Created by Greg Ziegan on 6/6/15.
 */
public class GoUpClickListener implements View.OnClickListener {

    private final Context context;

    public GoUpClickListener(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        NavUtils.navigateUpFromSameTask((Activity) context);
    }
}

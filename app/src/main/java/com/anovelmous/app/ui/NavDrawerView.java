package com.anovelmous.app.ui;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Greg Ziegan on 6/1/15.
 */
public final class NavDrawerView extends DrawerLayout {
    @InjectView(R.id.container) ViewGroup container;

    public NavDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            AnovelmousApp.get(context).inject(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);

        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        inflater.inflate(R.layout.activity_main, container);
    }

}

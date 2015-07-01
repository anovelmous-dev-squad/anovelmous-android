package com.anovelmous.app.ui;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anovelmous.app.R;
import com.fizzbuzz.android.dagger.InjectingActionBarActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;

/**
 * Created by Greg Ziegan on 6/4/15.
 */
public abstract class BaseActivity extends InjectingActionBarActivity {
    @Inject AppContainer appContainer;

    @InjectView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
    protected ObjectGraph mObjectGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Remove the status bar color. The DrawerLayout is responsible for drawing it from now on.
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ViewGroup container = appContainer.get(this);

        inflater.inflate(R.layout.frame_drawer_app, container);
        ButterKnife.inject(this, container);

        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.status_bar));
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
    }

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

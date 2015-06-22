package com.anovelmous.app.ui;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.InjectingActivityModule;
import com.anovelmous.app.Injector;
import com.anovelmous.app.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;

import static com.anovelmous.app.util.Preconditions.checkState;

/**
 * Created by Greg Ziegan on 6/4/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements Injector {
    @Inject AppContainer appContainer;

    @InjectView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
    private ObjectGraph mObjectGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ObjectGraph appGraph = AnovelmousApp.get(this).getObjectGraph();
        List<Object> activityModules = getModules();
        mObjectGraph = appGraph.plus(activityModules.toArray());

        inject(this);

        LayoutInflater inflater = LayoutInflater.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Remove the status bar color. The DrawerLayout is responsible for drawing it from now on.
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        AnovelmousApp app = AnovelmousApp.get(this);
        app.inject(this);

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

    @Override
    protected void onDestroy() {
        mObjectGraph = null;
        super.onDestroy();
    }

    @Override
    public final ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    @Override
    public void inject(Object target) {
        checkState(mObjectGraph != null, "object graph must be assigned prior to calling inject");
        mObjectGraph.inject(target);
    }

    protected List<Object> getModules() {
        List<Object> result = new ArrayList<>();
        result.add(new InjectingActivityModule(this, this));
        return result;
    }
}

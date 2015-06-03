package com.anovelmous.app.ui;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChapterSelectActivity extends AppCompatActivity {
    @Inject AppContainer appContainer;

    @InjectView(R.id.main_drawer_layout) DrawerLayout drawerLayout;
    @InjectView(R.id.main_content) ViewGroup content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Remove the status bar color. The DrawerLayout is responsible for drawing it from now on.
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        AnovelmousApp app = AnovelmousApp.get(this);
        app.inject(this);

        ViewGroup container = appContainer.get(this);

        inflater.inflate(R.layout.main_activity, container);
        ButterKnife.inject(this, container);

        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.status_bar));
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        inflater.inflate(R.layout.trending_view, content);
    }
}

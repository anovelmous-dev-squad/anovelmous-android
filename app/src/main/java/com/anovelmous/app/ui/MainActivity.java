package com.anovelmous.app.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public final class MainActivity extends BaseActivity {
    @InjectView(R.id.main_content) ViewGroup content;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();

        inflater.inflate(R.layout.activity_novel_select, content);
    }
}


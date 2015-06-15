package com.anovelmous.app.ui.reading;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anovelmous.app.R;
import com.anovelmous.app.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

public final class ReadingActivity extends BaseActivity {
    @InjectView(R.id.main_content) ViewGroup content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.nav_drawer, content);
    }

}

package com.anovelmous.app.ui.chapters;

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
import com.anovelmous.app.ui.AppContainer;
import com.anovelmous.app.ui.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChapterSelectActivity extends BaseActivity {
    @InjectView(R.id.main_content) ViewGroup content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();

        inflater.from(this).inflate(R.layout.activity_chapter_select, content);
    }
}

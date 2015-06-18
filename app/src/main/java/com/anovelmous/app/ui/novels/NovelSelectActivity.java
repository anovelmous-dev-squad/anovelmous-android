package com.anovelmous.app.ui.novels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anovelmous.app.R;
import com.anovelmous.app.ui.BaseActivity;

import butterknife.InjectView;

public final class NovelSelectActivity extends BaseActivity {
    @InjectView(R.id.app_content) ViewGroup content;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();

        inflater.inflate(R.layout.activity_novel_select, content);
    }
}


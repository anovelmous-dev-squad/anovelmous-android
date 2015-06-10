package com.anovelmous.app.ui.chapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anovelmous.app.R;
import com.anovelmous.app.ui.BaseActivity;

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

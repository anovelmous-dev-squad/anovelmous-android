package com.anovelmous.app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.anovelmous.app.R;

import butterknife.InjectView;

public class MainActivity extends BaseActivity {
    @InjectView(R.id.main_content) ViewGroup content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = getLayoutInflater();
        inflater.inflate(R.layout.activity_main, content);
    }
}

package com.anovelmous.app.ui;

import com.anovelmous.app.R;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;

public class MainActivity extends ToolbarControlBaseActivity<ObservableScrollView> {
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected ObservableScrollView createScrollable() {
        return (ObservableScrollView) findViewById(R.id.scrollable);
    }
}

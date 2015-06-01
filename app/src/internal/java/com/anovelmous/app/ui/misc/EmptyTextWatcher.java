package com.anovelmous.app.ui.misc;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public class EmptyTextWatcher implements TextWatcher {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override public void afterTextChanged(Editable s) {
    }
}

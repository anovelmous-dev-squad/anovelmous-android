package com.anovelmous.app.data;

import android.content.Intent;

import com.anovelmous.app.data.prefs.BooleanPreference;
import com.anovelmous.app.ui.ExternalIntentActivity;

import javax.inject.Inject;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public class DebugIntentFactory extends IntentFactory {
    private final boolean isMockMode;
    private final BooleanPreference captureIntents;

    @Inject
    public DebugIntentFactory(@IsMockMode boolean isMockMode,
                              @CaptureIntents BooleanPreference captureIntents) {
        this.isMockMode = isMockMode;
        this.captureIntents = captureIntents;
    }

    @Override public Intent createUrlIntent(String url) {
        Intent baseIntent = super.createUrlIntent(url);
        if (!isMockMode || !captureIntents.get()) {
            return baseIntent;
        } else {
            return ExternalIntentActivity.createIntent(baseIntent);
        }
    }
}

package com.anovelmous.app.data;

import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Singleton
public class IntentFactory {
    @Inject
    public IntentFactory() {
    }

    public Intent createUrlIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }
}

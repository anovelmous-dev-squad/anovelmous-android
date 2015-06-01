package com.anovelmous.app.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public interface ActivityHierarchyServer extends Application.ActivityLifecycleCallbacks {
    ActivityHierarchyServer NONE = new ActivityHierarchyServer() {
        @Override public void onActivityCreated(Activity activity, Bundle bundle) {}
        @Override public void onActivityStarted(Activity activity) {}
        @Override public void onActivityResumed(Activity activity) {}
        @Override public void onActivityPaused(Activity activity) {}
        @Override public void onActivityStopped(Activity activity) {}
        @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}
        @Override public void onActivityDestroyed(Activity activity) {}
    };
}

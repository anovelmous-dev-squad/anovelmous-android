package com.anovelmous.app;

import android.app.Application;
import android.content.Context;

import com.anovelmous.app.data.LumberYard;
import com.anovelmous.app.ui.ActivityHierarchyServer;

import net.danlew.android.joda.JodaTimeAndroid;

import javax.inject.Inject;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public final class AnovelmousApp extends Application {
    private static Context mContext;
    private ObjectGraph objectGraph;

    @Inject ActivityHierarchyServer activityHierarchyServer;
    @Inject LumberYard lumberYard;

    @Override public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {

        }

        buildObjectGraphAndInject();

        lumberYard.cleanUp();
        Timber.plant(lumberYard.tree());

        registerActivityLifecycleCallbacks(activityHierarchyServer);
    }

    public void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(Modules.list(this));
        objectGraph.inject(this);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    public static AnovelmousApp get(Context context) {
        return (AnovelmousApp) context.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}

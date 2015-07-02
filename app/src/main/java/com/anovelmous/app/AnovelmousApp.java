package com.anovelmous.app;

import android.app.Application;
import android.content.Context;

import com.anovelmous.app.data.LumberYard;
import com.anovelmous.app.ui.ActivityHierarchyServer;
import com.fizzbuzz.android.dagger.Injector;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;
import timber.log.Timber;

import static com.fizzbuzz.android.dagger.Preconditions.checkState;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public final class AnovelmousApp extends Application implements Injector {
    private ObjectGraph mObjectGraph;

    @Inject ActivityHierarchyServer activityHierarchyServer;
    @Inject LumberYard lumberYard;

    @Override public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
        //LeakCanary.install(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {

        }

        mObjectGraph = ObjectGraph.create(getModules().toArray());
        mObjectGraph.inject(this);

        lumberYard.cleanUp();
        Timber.plant(lumberYard.tree());

        registerActivityLifecycleCallbacks(activityHierarchyServer);
    }

    @Override
    public ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    public void inject(Object target) {
        checkState(mObjectGraph != null, "object graph must be initialized prior to calling inject");
        mObjectGraph.inject(target);
    }

    public static AnovelmousApp get(Context context) {
        return (AnovelmousApp) context.getApplicationContext();
    }

    protected List<Object> getModules() {
        List<Object> result = new ArrayList<>();
        result.addAll(Arrays.asList(Modules.list(this)));
        return result;
    }
}

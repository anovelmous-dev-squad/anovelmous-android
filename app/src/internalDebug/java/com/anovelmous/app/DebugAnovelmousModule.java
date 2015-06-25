package com.anovelmous.app;

import android.app.Application;
import android.content.Context;

import com.anovelmous.app.data.DebugDataModule;
import com.anovelmous.app.ui.DebugUiModule;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Module(
    addsTo = AnovelmousModule.class,
    includes = {
        DebugUiModule.class,
        DebugDataModule.class
    },
    overrides = true
)
public final class DebugAnovelmousModule {
    @Provides Context provideContext(Application app) {
        return app.getApplicationContext();
    }
}

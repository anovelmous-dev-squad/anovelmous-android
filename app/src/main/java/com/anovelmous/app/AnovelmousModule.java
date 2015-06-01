package com.anovelmous.app;

import android.app.Application;

import com.anovelmous.app.data.DataModule;
import com.anovelmous.app.ui.UiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Module(
    includes = {
        UiModule.class,
        DataModule.class
    },
    injects = {
        AnovelmousApp.class
    }
)
public final class AnovelmousModule {
    private final AnovelmousApp app;

    public AnovelmousModule(AnovelmousApp app) {
        this.app = app;
    }

    @Provides @Singleton Application provideApplication() {
        return app;
    }
}

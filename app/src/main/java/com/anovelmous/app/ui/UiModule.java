package com.anovelmous.app.ui;

import com.anovelmous.app.ui.trending.TrendingView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Module(
    injects = {
        MainActivity.class,
        TrendingView.class
    },
    complete = false,
    library = true
)
public class UiModule {
    @Provides @Singleton AppContainer provideAppContainer() {
        return AppContainer.DEFAULT;
    }

    @Provides @Singleton ActivityHierarchyServer provideActivityHierarchyServer() {
        return ActivityHierarchyServer.NONE;
    }
}

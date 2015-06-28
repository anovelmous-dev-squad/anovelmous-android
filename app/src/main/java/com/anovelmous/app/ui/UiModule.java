package com.anovelmous.app.ui;

import com.anovelmous.app.ui.chapters.ChapterSelectFragment;
import com.anovelmous.app.ui.contribute.ContributeFragment;
import com.anovelmous.app.ui.novels.NovelSelectFragment;
import com.anovelmous.app.ui.reading.ReadingFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Module(
    injects = {
        MainActivity.class,
        LoggedInActivity.class,
        ReadingFragment.class,
        ContributeFragment.class,
        AboutFragment.class,
        NovelSelectFragment.class,
        ChapterSelectFragment.class,
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

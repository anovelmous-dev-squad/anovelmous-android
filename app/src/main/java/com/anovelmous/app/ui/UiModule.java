package com.anovelmous.app.ui;

import com.anovelmous.app.ui.chapters.ChapterSelectActivity;
import com.anovelmous.app.ui.chapters.ChapterSelectView;
import com.anovelmous.app.ui.novels.NovelSelectView;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Module(
    injects = {
        MainActivity.class,
        NovelSelectView.class,
        ChapterSelectActivity.class,
        ChapterSelectView.class
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

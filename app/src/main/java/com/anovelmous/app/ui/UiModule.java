package com.anovelmous.app.ui;

import com.anovelmous.app.ui.chapters.ChapterSelectActivity;
import com.anovelmous.app.ui.chapters.ChapterSelectView;
import com.anovelmous.app.ui.novels.NovelSelectFragment;
import com.anovelmous.app.ui.reading.ReadingActivity;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Module(
    injects = {
        ReadingActivity.class,
        NovelSelectFragment.class,
        ChapterSelectActivity.class,
        ChapterSelectView.class,
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

package com.anovelmous.app.ui;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module(
    overrides = true,
    library = true,
    complete = false
)
public final class InternalReleaseUiModule {
  @Provides @Singleton AppContainer provideAppContainer(
      TelescopeAppContainer telescopeAppContainer) {
    return telescopeAppContainer;
  }
}

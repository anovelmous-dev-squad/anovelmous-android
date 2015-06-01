package com.anovelmous.app;

import com.anovelmous.app.ui.InternalReleaseUiModule;
import dagger.Module;

@Module(
    addsTo = AnovelmousModule.class,
    includes = InternalReleaseUiModule.class,
    overrides = true
)
public final class InternalReleaseU2020Module {
}

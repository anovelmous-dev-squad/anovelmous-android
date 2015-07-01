package com.anovelmous.app;

import android.content.Context;

import com.anovelmous.app.data.DataModule;
import com.anovelmous.app.ui.UiModule;
import com.fizzbuzz.android.dagger.Injector;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

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
    },
    library = true
)
public final class AnovelmousModule {
    private final AnovelmousApp mApp;
    private final Injector mInjector;

    public AnovelmousModule(AnovelmousApp app, Injector injector) {
        this.mApp = app;
        this.mInjector = injector;
    }

    /**
     * Provides the Application Context associated with this module's graph.
     *
     * @return the Application Context
     */
    @Provides
    @Singleton
    @Application
    public Context provideApplicationContext() {
        return mApp.getApplicationContext();
    }

    /**
     * Provides the Application
     *
     * @return the Application
     */
    @Provides
    @Singleton
    public android.app.Application provideApplication() {
        return mApp;
    }

    /**
     * Provides the Injector for the Application-scope graph
     *
     * @return the Injector
     */
    @Provides
    @Singleton
    @Application
    public Injector provideApplicationInjector() {
        return mInjector;
    }

    /**
     * Defines an qualifier annotation which can be used in conjunction with a type to identify dependencies within
     * this module's object graph.
     *
     * @see <a href="http://square.github.io/dagger/">the dagger documentation</a>
     */
    @Qualifier
    @Target({FIELD, PARAMETER, METHOD})
    @Documented
    @Retention(RUNTIME)
    public @interface Application {
    }
}

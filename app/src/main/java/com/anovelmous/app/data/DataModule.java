package com.anovelmous.app.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.anovelmous.app.data.api.ApiModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import org.joda.time.DateTime;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.jakewharton.byteunits.DecimalByteUnit.MEGABYTES;
/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Module(
    includes = ApiModule.class,
    complete = false,
    library = true
)
public final class DataModule {
    static final int DISK_CACHE_SIZE = (int) MEGABYTES.toBytes(50);

    @Provides @Singleton SharedPreferences provideSharedPreferences(Application app) {
        return app.getSharedPreferences("Anovelmous", Context.MODE_PRIVATE);
    }

    @Provides @Singleton Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeConverter())
                .create();
    }

    @Provides @Singleton Clock provideClock() {
        return Clock.REAL;
    }

    @Provides @Singleton OkHttpClient provideOkHttpClient(Application app) {
        return createOkHttpClient(app);
    }

    static OkHttpClient createOkHttpClient(Application app) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);

        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        client.setCache(cache);

        return client;
    }
}

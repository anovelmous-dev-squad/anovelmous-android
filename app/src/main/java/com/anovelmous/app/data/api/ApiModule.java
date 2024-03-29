package com.anovelmous.app.data.api;

import android.app.Application;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Module(
    complete = false,
    library = true
)
public final class ApiModule {
    public static final String PRODUCTION_API_URL = "http://anovelmous.com/api";

    @Provides @Singleton Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(PRODUCTION_API_URL);
    }

    @Provides @Singleton @Named("Api") OkHttpClient provideApiClient(OkHttpClient client) {
        return client.clone();
    }

    @Provides @Singleton
    RestAdapter provideRestAdapter(Endpoint endpoint,
          @Named("Api") OkHttpClient client, Gson gson) {
        return new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(endpoint)
                .setConverter(new GsonConverter(gson))
                .build();
    }

    @Provides @Singleton
    NetworkService provideNetworkService(RestAdapter restAdapter) {
        return restAdapter.create(NetworkService.class);
    }

    @Provides @Singleton
    PersistenceService provideDataService(Application app) {
        return new RealmPersistenceService(app);
    }

    @Provides @Singleton RestService provideRestService(NetworkService networkService,
                                                        PersistenceService persistenceService) {
        return new AnovelmousService(networkService, persistenceService);
    }
}

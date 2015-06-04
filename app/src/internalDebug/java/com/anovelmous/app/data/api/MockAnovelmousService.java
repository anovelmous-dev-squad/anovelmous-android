package com.anovelmous.app.data.api;

import android.content.SharedPreferences;

import com.anovelmous.app.data.api.model.ChaptersResponse;
import com.anovelmous.app.data.api.model.NovelsResponse;
import com.anovelmous.app.util.EnumPreferences;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Singleton
public final class MockAnovelmousService implements AnovelmousService {
    private final SharedPreferences preferences;
    private final Map<Class<? extends Enum<?>>, Enum<?>> responses = new LinkedHashMap<>();

    @Inject
    MockAnovelmousService(SharedPreferences preferences) {
        this.preferences = preferences;

        loadResponse(MockNovelsResponse.class, MockNovelsResponse.SUCCESS);
        loadResponse(MockChaptersResponse.class, MockChaptersResponse.SUCCESS);
    }

    /**
     * Initializes the current response for {@code responseClass} from {@code SharedPreferences}, or
     * uses {@code defaultValue} if a response was not found.
     */
    private <T extends Enum<T>> void loadResponse(Class<T> responseClass, T defaultValue) {
        responses.put(responseClass, EnumPreferences.getEnumValue(preferences, responseClass, //
                responseClass.getCanonicalName(), defaultValue));
    }

    public <T extends Enum<T>> T getResponse(Class<T> responseClass) {
        return responseClass.cast(responses.get(responseClass));
    }

    public <T extends Enum<T>> void setResponse(Class<T> responseClass, T value) {
        responses.put(responseClass, value);
        EnumPreferences.saveEnumValue(preferences, responseClass.getCanonicalName(), value);
    }

    @Override public Observable<NovelsResponse> novels(@Query("sort") Sort sort,
                                                       @Query("order") Order order) {
        NovelsResponse response = getResponse(MockNovelsResponse.class).response;
        SortUtil.sort(response.items, sort, order);

        return Observable.just(response);
    }

    @Override public Observable<ChaptersResponse> chapters(@Query("novel") long novelId,
                                                 @Query("sort") Sort sort,
                                                 @Query("order") Order order) {
        ChaptersResponse response = getResponse(MockChaptersResponse.class).response;
        // TODO: sorting chapters

        return Observable.just(response);
    }
}

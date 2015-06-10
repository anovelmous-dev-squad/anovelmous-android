package com.anovelmous.app.data.api;

import android.content.SharedPreferences;

import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.response.ChapterTextResponse;
import com.anovelmous.app.data.api.response.ChaptersResponse;
import com.anovelmous.app.data.api.response.NovelsResponse;
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
public final class MockNetworkService implements NetworkService {
    private static final int NOVEL_COUNT = 3;
    private static final int CHAPTER_COUNT = 10;

    private final SharedPreferences preferences;
    private final Map<Class<? extends Enum<?>>, Enum<?>> responses = new LinkedHashMap<>();

    @Inject
    MockNetworkService(SharedPreferences preferences) {
        this.preferences = preferences;

        loadResponse(MockNovelsResponse.class, MockNovelsResponse.SUCCESS);
        loadResponse(MockChaptersResponse.class, MockChaptersResponse.SUCCESS);
        loadResponse(MockChapterTextResponse.class, MockChapterTextResponse.SUCCESS);
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

    @Override public Observable<NovelsResponse> novels(@Query("page_size") int pageSize,
                                                       @Query("page") int page,
                                                       @Query("sort") Sort sort,
                                                       @Query("order") Order order) {
        NovelsResponse response = getResponse(MockNovelsResponse.class).response;
        NovelUtil.sort(response.items, sort, order);

        return Observable.just(response);
    }

    @Override
    public Observable<ResourceCount> novelsCount() {
        return Observable.just(new ResourceCount.Builder().count(NOVEL_COUNT).build());
    }

    @Override public Observable<ChaptersResponse> chapters(@Query("novel") long novelId,
                                                           @Query("sort") Sort sort,
                                                           @Query("order") Order order) {
        ChaptersResponse response = getResponse(MockChaptersResponse.class).response;
        ChapterUtil.sort(response.items, sort, order);

        return Observable.just(response);
    }

    @Override
    public Observable<ResourceCount> chaptersCount() {
        return Observable.just(new ResourceCount.Builder().count(CHAPTER_COUNT).build());
    }

    @Override public Observable<ChapterTextResponse> chapterText(@Query("chapter") long chapterId,
                                                                 @Query("sort") Sort sort,
                                                                 @Query("order") Order order) {
        ChapterTextResponse response = getResponse(MockChapterTextResponse.class).response;
        FormattedNovelTokenUtil.sort(response.items, sort, order);

        return Observable.just(response);
    }
}

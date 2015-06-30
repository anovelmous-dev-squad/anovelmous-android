package com.anovelmous.app.data.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Contributor;
import com.anovelmous.app.data.api.resource.NovelToken;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.Vote;
import com.anovelmous.app.data.api.response.ChapterTextResponse;
import com.anovelmous.app.data.api.response.ChaptersResponse;
import com.anovelmous.app.data.api.response.NovelsResponse;
import com.anovelmous.app.util.EnumPreferences;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.http.Body;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Singleton
public final class MockNetworkService implements NetworkService {
    private static final int NOVEL_COUNT = 3;
    private static final int CHAPTER_COUNT = 10;
    private static final int CHAPTER_TEXT_TOKEN_COUNT = 21;
    private static final int TOKEN_COUNT = 19;

    private final SharedPreferences preferences;
    private final Context context;
    private final Map<Class<? extends Enum<?>>, Enum<?>> responses = new LinkedHashMap<>();

    @Inject
    MockNetworkService(SharedPreferences preferences, Context context) {
        this.preferences = preferences;
        this.context = context;

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

    @Override public Observable<ChaptersResponse> chapters(@Query("novel") String novelId,
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

    @Override public Observable<ChapterTextResponse> chapterText(@Query("chapter") String chapterId,
                                                                 @Query("sort") Sort sort,
                                                                 @Query("order") Order order) {
        ChapterTextResponse response = getResponse(MockChapterTextResponse.class).response;
        FormattedNovelTokenUtil.sort(response.items, sort, order);

        return Observable.just(response);
    }

    @Override
    public Observable<ResourceCount> chapterTextTokenCount(@Query("chapter") String chapterId) {
        return Observable.just(new ResourceCount.Builder().count(CHAPTER_TEXT_TOKEN_COUNT).build());
    }

    @Override
    public Observable<Token> getTokenByContent(@Query("content") String content) {
        return Observable.just(new Token.Builder()
                .id("1")
                .url("mock://tokens/1")
                .content(content)
                .isPunctuation(false)
                .isValid(true)
                .build());
    }

    @Override
    public Observable<List<Token>> tokens(@Query("sort") Sort sort, @Query("order") Order order) {
        List<Token> tokens = new ArrayList<>();
        return Observable.just(tokens); // TODO: craft mock data
    }

    @Override
    public Observable<ResourceCount> tokensCount() {
        return Observable.just(new ResourceCount.Builder().count(TOKEN_COUNT).build());
    }

    @Override
    public Observable<List<String>> getGrammarFilteredWords() {
        List<String> tokens = Arrays.asList(context.getResources().getStringArray(R.array.mock_token_set));
        return Observable.just(tokens);
    }

    @Override
    public Observable<Vote> castVote(@Path("client_id") String clientId, @Body Vote vote) {
        return Observable.just(vote);
    }

    @Override
    public Observable<Contributor> getContributor(@Path("id") String userId) {
        return Observable.just(new Contributor.Builder()
                .id(userId)
                .url("mock://users/" + userId)
                .restVerb(RestVerb.GET)
                .username("Mock Contributor")
                .email("grz5@case.edu") // TODO: fix mock Contributor building process
                .groups(new ArrayList<String>())
                .dateJoined(new DateTime())
                .build());
    }

    @Override
    public Observable<Contributor> createContributor(@Path("client_id") String clientId, @Body Contributor contributor) {
        return Observable.just(new Contributor.Builder()
                .id(clientId)
                .url("mock://users/" + clientId)
                .restVerb(RestVerb.POST)
                .username("Mock Contributor")
                .email("grz5@case.edu")
                .groups(new ArrayList<String>())
                .dateJoined(new DateTime())
                .build());
    }

    @Override
    public Observable<NovelToken> getMostRecentNovelToken(@Query("chapter") String chapterId,
                                                          @Query("sort") Sort sort,
                                                          @Query("order") Order order) {
        return Observable.just(new NovelToken.Builder()
                        .id("1")
                        .url("mock://novel_tokens/1")
                        .restVerb(RestVerb.GET)
                        .chapter("mock://chapters/1")
                        .ordinal(1)
                        .token("mock://tokens/1")
                        .build());
    }
}

package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Contributor;
import com.anovelmous.app.data.api.resource.NovelToken;
import com.anovelmous.app.data.api.resource.ResourceCount;
import com.anovelmous.app.data.api.resource.Token;
import com.anovelmous.app.data.api.resource.Vote;
import com.anovelmous.app.data.api.response.ChapterTextResponse;
import com.anovelmous.app.data.api.response.ChaptersResponse;
import com.anovelmous.app.data.api.response.NovelsResponse;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public interface NetworkService {
    @GET("/novels")
    Observable<NovelsResponse> novels(
            @Query("page_size") int pageSize,
            @Query("page") int page,
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/novels?page_size=1")
    Observable<ResourceCount> novelsCount();

    @GET("/chapters")
    Observable<ChaptersResponse> chapters(
            @Query("novel") String novelId,
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/chapters?page_size=1")
    Observable<ResourceCount> chaptersCount();

    @GET("/formatted_novel_tokens")
    Observable<ChapterTextResponse> chapterText(
            @Query("chapter") String chapterId,
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/formatted_novel_tokens?page_size=1")
    Observable<ResourceCount> chapterTextTokenCount(@Query("chapter") String chapterId);

    @GET("/tokens")
    Observable<List<Token>> tokens(
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/tokens")
    Observable<Token> getTokenByContent(@Query("content") String content);

    @GET("/tokens?page_size=1")
    Observable<ResourceCount> tokensCount();

    @GET("/tokens/filter_on_grammar")
    Observable<List<String>> getGrammarFilteredWords();

    @PUT("/votes/{id}")
    Observable<Vote> castVote(@Path("id") String id, @Body Vote vote);

    @GET("/users/{id}")
    Observable<Contributor> getContributor(@Path("id") String userId);

    @PUT("/users/{id}")
    Observable<Contributor> createContributor(@Path("id") String userId, @Body Contributor contributor);

    @GET("/novel_tokens")
    Observable<NovelToken> getMostRecentNovelToken(
            @Query("chapter") String chapterId,
            @Query("sort") Sort sort,
            @Query("order") Order order);
}

package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.Vote;
import com.anovelmous.app.data.api.response.ChapterTextResponse;
import com.anovelmous.app.data.api.response.ChaptersResponse;
import com.anovelmous.app.data.api.response.NovelsResponse;
import com.anovelmous.app.data.api.resource.ResourceCount;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
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
            @Query("novel") long novelId,
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/chapters?page_size=1")
    Observable<ResourceCount> chaptersCount();

    @GET("/formatted_novel_tokens")
    Observable<ChapterTextResponse> chapterText(
            @Query("chapter") long chapterId,
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/formatted_novel_tokens?page_size=1")
    Observable<ResourceCount> chapterTextTokenCount(@Query("chapter") long chapterId);

    @POST("/votes")
    Observable<Vote> postVote(@Body Vote vote);
}

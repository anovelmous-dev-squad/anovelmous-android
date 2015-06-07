package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.resource.ChapterTextResponse;
import com.anovelmous.app.data.api.resource.ChaptersResponse;
import com.anovelmous.app.data.api.resource.NovelsResponse;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
public interface AnovelmousService {
    @GET("/novels")
    Observable<NovelsResponse> novels(
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/chapters")
    Observable<ChaptersResponse> chapters(
            @Query("novel") long id,
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/formatted_novel_tokens")
    Observable<ChapterTextResponse> chapterText(
            @Query("chapter") long id,
            @Query("sort") Sort sort,
            @Query("order") Order order);
}

package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.response.ChapterTextResponse;
import com.anovelmous.app.data.api.response.ChaptersResponse;
import com.anovelmous.app.data.api.response.NovelsResponse;
import com.anovelmous.app.data.api.resource.ResourceCount;

import retrofit.http.GET;
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
            @Query("novel") long id,
            @Query("sort") Sort sort,
            @Query("order") Order order);

    @GET("/formatted_novel_tokens")
    Observable<ChapterTextResponse> chapterText(
            @Query("chapter") long id,
            @Query("sort") Sort sort,
            @Query("order") Order order);
}

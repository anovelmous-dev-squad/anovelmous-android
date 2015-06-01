package com.anovelmous.app.data.api;

import com.anovelmous.app.data.api.model.NovelsResponse;

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
}

package com.example.zoointroduction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("apiAccess?scope=resourceAquire")
    Call<ZooApiResponse> getAnswers(@Query("offset") int offset, @Query("limit") int limit, @Query("rid") String rid, @Query("q") String query);
}

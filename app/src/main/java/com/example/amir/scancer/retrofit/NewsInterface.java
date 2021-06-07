package com.example.amir.scancer.retrofit;

import com.example.amir.scancer.models.News;
import com.example.amir.scancer.models.NewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsInterface {

    @GET("/v2/top-headlines")
    Call<NewsResponse> getLatestNews(@Query("sources") String source, @Query("apiKey") String apiKey);

}

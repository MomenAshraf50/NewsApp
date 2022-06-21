package com.example.newsapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SearchInNews {
    @GET("v2/everything?from=2021-08-13&sortBy=publishedAt&apiKey=cc64bb2eb8ec499789f3c65130c17fb1")
    Call<NewsResponse> getNews(@Query("q") String q);
}

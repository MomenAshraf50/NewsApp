package com.example.newsapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApi {
    @GET("v2/top-headlines?sortBy=publishedAt&country=eg&apiKey=cc64bb2eb8ec499789f3c65130c17fb1")
    Call<NewsResponse> getNews(@Query("category") String category);
}

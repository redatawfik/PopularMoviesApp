package com.popular_movies_app_st_1.app.rest;


import com.popular_movies_app_st_1.app.model.MovieTrailersResponse;
import com.popular_movies_app_st_1.app.model.MoviesResponse;
import com.popular_movies_app_st_1.app.model.ReviewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {



    @GET("movie/{sort_by}")
    Call<MoviesResponse> getMoviesList(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);


    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getReviewsList(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailersResponse> getVideosList(@Path("id") int id, @Query("api_key") String apiKey);
}

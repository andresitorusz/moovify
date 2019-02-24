package com.stilldre.moovify.service.network.api;

import com.stilldre.moovify.model.Movie;
import com.stilldre.moovify.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDb {
    @GET("search/movie")
    Call<MovieResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String movieTitle
    );

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(
            @Query("api_key") String apiKey
    );

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies(
            @Query("api_key") String apiKey
    );

    @GET("movie/{movie_id}")
    Call<Movie> getDetail(
            @Path("movie_id") String id,
            @Query("api_key") String apiKEy
    );
}

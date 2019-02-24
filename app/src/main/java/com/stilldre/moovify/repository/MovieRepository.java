package com.stilldre.moovify.repository;

import com.stilldre.moovify.BuildConfig;
import com.stilldre.moovify.model.Movie;
import com.stilldre.moovify.model.MovieResponse;
import com.stilldre.moovify.service.network.callback.OnGetDetailCallback;
import com.stilldre.moovify.service.network.callback.OnGetMoviesCallback;
import com.stilldre.moovify.service.network.api.TMDb;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository {

    String API_KEY = BuildConfig.TMDB_API_KEY;
    static String BASE_URL = BuildConfig.TMDB_BASE_URL;

    private static MovieRepository movieRepository;
    private TMDb api;

    public MovieRepository(TMDb api) {
        this.api = api;
    }

    public static MovieRepository getInstance() {
        if (movieRepository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            movieRepository = new MovieRepository(retrofit.create(TMDb.class));
        }
        return movieRepository;
    }

    public void getNowPlayingMovie(final OnGetMoviesCallback callback) {
        api.getNowPlayingMovies(API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse movieResponse = response.body();
                            if (movieResponse != null && movieResponse.getMovies() != null) {
                                callback.onSuccess(movieResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getUpcomingMovie(final OnGetMoviesCallback callback) {
        api.getUpcomingMovies(API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse movieResponse = response.body();
                            if (movieResponse != null && movieResponse.getMovies() != null) {
                                callback.onSuccess(movieResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getDetail(String movieId, final OnGetDetailCallback callback) {
        api.getDetail(movieId, API_KEY)
                .enqueue(new Callback<Movie>() {
                             @Override
                             public void onResponse(Call<Movie> call, Response<Movie> response) {

                                 if (response.isSuccessful()) {
                                     Movie movie = response.body();
                                     if (movie != null) {
                                         callback.onSuccess(movie);
                                     } else {
                                         callback.onError();
                                     }
                                 } else {
                                     callback.onError();
                                 }
                             }

                             @Override
                             public void onFailure(Call<Movie> call, Throwable t) {
                                 callback.onError();
                             }
                         }

                );
    }

    public void searchMovie(String movieTitle, final OnGetMoviesCallback callback) {
        api.searchMovies(API_KEY, movieTitle)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (response.isSuccessful()) {
                            MovieResponse movieResponse = response.body();
                            if (movieResponse != null && movieResponse.getMovies() != null) {
                                callback.onSuccess(movieResponse.getMovies());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}

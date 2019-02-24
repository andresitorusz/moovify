package com.stilldre.moovify.service.network.callback;

import com.stilldre.moovify.model.Movie;

import java.util.List;

public interface OnGetMoviesCallback {
    void onSuccess(List<Movie> movies);

    void onError();
}

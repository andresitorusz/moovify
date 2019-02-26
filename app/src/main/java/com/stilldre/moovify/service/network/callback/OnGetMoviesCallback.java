package com.stilldre.moovify.service.network.callback;

import com.stilldre.moovify.model.Movie;

import java.util.ArrayList;

public interface OnGetMoviesCallback {
    void onSuccess(ArrayList<Movie> movies);

    void onError();
}

package com.stilldre.moovify.service.network.callback;

import com.stilldre.moovify.model.Movie;

public interface OnGetDetailCallback {

    void onSuccess(Movie movie);

    void onError();
}

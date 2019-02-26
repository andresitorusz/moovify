package com.stilldre.moovify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MovieResponse {
    @SerializedName("results")
    @Expose
    private ArrayList<Movie> movies;

    public MovieResponse(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

}

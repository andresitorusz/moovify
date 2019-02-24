package com.stilldre.moovify.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stilldre.moovify.BuildConfig;
import com.stilldre.moovify.R;
import com.stilldre.moovify.model.Movie;
import com.stilldre.moovify.service.click.OnMovieClickCallback;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private String IMAGE_BASE_URL = BuildConfig.TMDB_IMAGE_BASE_URL_500;
    private List<Movie> movies;
    private OnMovieClickCallback callback;

    public MovieAdapter(List<Movie> movies, OnMovieClickCallback callback) {
        this.movies = movies;
        this.callback = callback;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_catalogue, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.bind(movies.get(i));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView tvReleaseDate;
        TextView tvTitle;
        TextView tvRating;
        ImageView ivPoster;

        Movie movie;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReleaseDate = itemView.findViewById(R.id.item_movie_release_date);
            tvTitle = itemView.findViewById(R.id.item_movie_title);
            tvRating = itemView.findViewById(R.id.item_movie_rating);
            ivPoster = itemView.findViewById(R.id.item_movie_poster);

            itemView.setOnClickListener(v -> callback.onClick(movie));
        }

        public void bind(Movie movie) {
            tvReleaseDate.setText(movie.getReleaseDate().split("-")[0]);
            tvTitle.setText(movie.getTitle());
            tvRating.setText(String.valueOf(movie.getRating()));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(ivPoster);
            this.movie = movie;
        }
    }
}

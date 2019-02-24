package com.stilldre.favoritemodule.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stilldre.favoritemodule.BuildConfig;
import com.stilldre.favoritemodule.R;
import com.stilldre.favoritemodule.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private String IMAGE_BASE_URL = BuildConfig.TMDB_IMAGE_BASE_URL_500;
    private Context context;
    private Cursor cursor;

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    public void setFavoriteMovies(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movie_catalogue, viewGroup, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int i) {
        Movie movie = getItem(i);
        holder.tvReleaseDate.setText(movie.getReleaseDate().split("-")[0]);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvRating.setText(String.valueOf(movie.getRating()));
        Glide.with(context)
                .load(IMAGE_BASE_URL + movie.getPosterPath())
                .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                .into(holder.ivPoster);
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position Invalid");
        }
        return new Movie(cursor);
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_movie_title)
        TextView tvTitle;
        @BindView(R.id.item_movie_rating)
        TextView tvRating;
        @BindView(R.id.item_movie_release_date)
        TextView tvReleaseDate;
        @BindView(R.id.item_movie_poster)
        ImageView ivPoster;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.stilldre.favoritemodule.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static com.stilldre.favoritemodule.database.DatabaseContract.FavoriteColumns.COLUMN_BACKDROP_PATH;
import static com.stilldre.favoritemodule.database.DatabaseContract.FavoriteColumns.COLUMN_ID_MOVIE;
import static com.stilldre.favoritemodule.database.DatabaseContract.FavoriteColumns.COLUMN_OVERVIEW;
import static com.stilldre.favoritemodule.database.DatabaseContract.FavoriteColumns.COLUMN_POSTER_PATH;
import static com.stilldre.favoritemodule.database.DatabaseContract.FavoriteColumns.COLUMN_RATING;
import static com.stilldre.favoritemodule.database.DatabaseContract.FavoriteColumns.COLUMN_RELEASE_DATE;
import static com.stilldre.favoritemodule.database.DatabaseContract.FavoriteColumns.COLUMN_TITLE;
import static com.stilldre.favoritemodule.database.DatabaseContract.getColumnFloat;
import static com.stilldre.favoritemodule.database.DatabaseContract.getColumnString;

public class Movie implements Parcelable {
    @SerializedName("id")
    @Expose
    private String movieId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("vote_average")
    @Expose
    private float rating;

    public Movie(String id, String title, String posterPath, String backdropPath, String overview, String releaseDate, float rating) {
        this.movieId = id;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        movieId = in.readString();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        rating = in.readFloat();
    }

    public Movie(Cursor cursor) {
        this.movieId = getColumnString(cursor, COLUMN_ID_MOVIE);
        this.title = getColumnString(cursor, COLUMN_TITLE);
        this.posterPath = getColumnString(cursor, COLUMN_POSTER_PATH);
        this.backdropPath = getColumnString(cursor, COLUMN_BACKDROP_PATH);
        this.overview = getColumnString(cursor, COLUMN_OVERVIEW);
        this.releaseDate = getColumnString(cursor, COLUMN_RELEASE_DATE);
        this.rating = getColumnFloat(cursor, COLUMN_RATING);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public float getRating() {
        return rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeFloat(rating);
    }

    @Override
    public String toString() {
        return "FavoriteHelper{" +
                "id=" + movieId +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", rating=" + rating +
                '}';
    }
}

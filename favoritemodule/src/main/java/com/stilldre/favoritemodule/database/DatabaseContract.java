package com.stilldre.favoritemodule.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.stilldre.favoritemodule.BuildConfig;

public class DatabaseContract {
    public static String TABLE_NAME = BuildConfig.FAVORITE_TABLE_MOVIE;
    public static String AUTHORITY = BuildConfig.FAVORITE_AUTHORITY;

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build();

    public static final class FavoriteColumns implements BaseColumns {
        public static String COLUMN_ID_MOVIE = "movie_id";
        public static String COLUMN_TITLE = "movie_title";
        public static String COLUMN_POSTER_PATH = "poster_path";
        public static String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static String COLUMN_OVERVIEW = "overview";
        public static String COLUMN_RATING = "rating";
        public static String COLUMN_RELEASE_DATE = "release_date";
    }

    public DatabaseContract() {
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static float getColumnFloat(Cursor cursor, String columnName) {
        return cursor.getFloat(cursor.getColumnIndex(columnName));
    }
}

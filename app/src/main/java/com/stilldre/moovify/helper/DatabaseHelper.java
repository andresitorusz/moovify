package com.stilldre.moovify.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stilldre.moovify.database.DatabaseContract;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "favorite.db";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_TABLE_FAVORITE = "CREATE TABLE " + DatabaseContract.TABLE_NAME + " (" +
                DatabaseContract.FavoriteColumns.COLUMN_ID_MOVIE + " TEXT PRIMARY KEY," +
                DatabaseContract.FavoriteColumns.COLUMN_TITLE + " TEXT NOT NULL," +
                DatabaseContract.FavoriteColumns.COLUMN_POSTER_PATH + " TEXT NOT NULL," +
                DatabaseContract.FavoriteColumns.COLUMN_BACKDROP_PATH + " TEXT," +
                DatabaseContract.FavoriteColumns.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                DatabaseContract.FavoriteColumns.COLUMN_RATING + " TEXT NOT NULL," +
                DatabaseContract.FavoriteColumns.COLUMN_RELEASE_DATE + " TEXT NOT NULL); ";
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NAME);
        onCreate(db);
    }
}

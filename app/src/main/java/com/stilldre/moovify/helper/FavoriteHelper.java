package com.stilldre.moovify.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.stilldre.moovify.model.Movie;

import static com.stilldre.moovify.database.DatabaseContract.FavoriteColumns.COLUMN_BACKDROP_PATH;
import static com.stilldre.moovify.database.DatabaseContract.FavoriteColumns.COLUMN_ID_MOVIE;
import static com.stilldre.moovify.database.DatabaseContract.FavoriteColumns.COLUMN_OVERVIEW;
import static com.stilldre.moovify.database.DatabaseContract.FavoriteColumns.COLUMN_POSTER_PATH;
import static com.stilldre.moovify.database.DatabaseContract.FavoriteColumns.COLUMN_RATING;
import static com.stilldre.moovify.database.DatabaseContract.FavoriteColumns.COLUMN_RELEASE_DATE;
import static com.stilldre.moovify.database.DatabaseContract.FavoriteColumns.COLUMN_TITLE;
import static com.stilldre.moovify.database.DatabaseContract.TABLE_NAME;

public class FavoriteHelper {

    private static final String DATABASE_TABLE = TABLE_NAME;
    protected Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    public FavoriteHelper(Context context) {
        this.context = context.getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
    }

    public FavoriteHelper open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen()) {
            database.close();
        }
    }

//    public ArrayList<Movie> getAllMovies() {
//        ArrayList<Movie> arrayList = new ArrayList<>();
//        open();
//        Cursor cursor = database.query(DATABASE_TABLE, null,
//                null,
//                null,
//                null,
//                null,
//                COLUMN_ID_MOVIE + " ASC",
//                null);
//        cursor.moveToFirst();
//        Movie movie;
//        if (cursor.getCount() > 0) {
//            do {
//                movie = new Movie();
//
//                movie.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ID_MOVIE)));
//                movie.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.COLUMN_TITLE)));
//                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.COLUMN_POSTER_PATH)));
//                movie.setBackdropPath(cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.COLUMN_BACKDROP_PATH)));
//                movie.setOverview(cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.COLUMN_OVERVIEW)));
//                movie.setRating(cursor.getFloat(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.COLUMN_RATING)));
//                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.FavoriteColumns.COLUMN_RELEASE_DATE)));
//
//                arrayList.add(movie);
//                cursor.moveToNext();
//            } while (!cursor.isAfterLast());
//        }
//        cursor.close();
//        close();
//        return arrayList;
//    }

    public long insertMovie(Movie movie) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_ID_MOVIE, movie.getId());
        args.put(COLUMN_TITLE, movie.getTitle());
        args.put(COLUMN_POSTER_PATH, movie.getPosterPath());
        args.put(COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        args.put(COLUMN_OVERVIEW, movie.getOverview());
        args.put(COLUMN_RATING, String.valueOf(movie.getRating()));
        args.put(COLUMN_RELEASE_DATE, movie.getReleaseDate());

        return database.insertWithOnConflict(DATABASE_TABLE, null, args, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean getMovieById(String id){
        Cursor cursor;
        open();
        String query = "SELECT * FROM favorite WHERE movie_id = '" + id + "'";
        cursor = database.rawQuery(query,null);
        boolean isFavorite = false;

        if (cursor != null && (cursor.getCount() > 0)) {
            isFavorite = true;
            cursor.close();
        }
        return isFavorite;
    }

    public int deleteFavorite(String id) {
        return database.delete(DATABASE_TABLE, COLUMN_ID_MOVIE + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String movieId) {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID_MOVIE + " ASC");
    }

    public long insertProvider(ContentValues values) {
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int updateProvider(String movieId, ContentValues values) {
        return database.update(DATABASE_TABLE, values, COLUMN_ID_MOVIE + " = ?", new String[]{movieId});
    }

    public int deleteProvider(String movieId) {
        return database.delete(DATABASE_TABLE, COLUMN_ID_MOVIE + " = ?", new String[]{movieId});
    }
}

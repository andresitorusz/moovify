package com.stilldre.moovify.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.stilldre.moovify.BuildConfig;
import com.stilldre.moovify.R;
import com.stilldre.moovify.model.Movie;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static com.stilldre.moovify.database.DatabaseContract.CONTENT_URI;

public class MoovifyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static String MOVIE_POSTER = BuildConfig.TMDB_IMAGE_BASE_URL_500;
    private final Context context;
    private int appWidgetId;
    private Cursor cursor;

    public MoovifyRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        if (cursor != null) {
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor != null) cursor.close();
    }

    @Override
    public int getCount() {
        if (cursor == null) return 0;
        else return cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION || cursor == null || !cursor.moveToPosition(position)) {
            return null;
        }

        Movie movie = getItem(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.moovify_favorite_item);

        Bitmap bitmap = null;
        String posterPath = MOVIE_POSTER + movie.getPosterPath();
        String movieTitle = movie.getTitle();
        String date = dateFormat(movie.getReleaseDate());

        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(posterPath)
                    .submit()
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);

        Bundle bundle = new Bundle();
        bundle.putString(MoovifyFavoriteWidget.EXTRA_ITEM, movieTitle + "\n" + date);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(bundle);

        remoteViews.setOnClickFillInIntent(R.id.imageView, fillIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        if (cursor.moveToPosition(position)) {
            return cursor.getLong(0);
        } else return position;
    }

    private Movie getItem(int position) {
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("Position Invalid");
        }
        return new Movie(cursor);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private String dateFormat(String oldDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = null;
        try {
            myDate = dateFormat.parse(oldDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy");
        String finalDate = newFormat.format(myDate);

        return finalDate;
    }
}

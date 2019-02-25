package com.stilldre.moovify.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.stilldre.moovify.BuildConfig;
import com.stilldre.moovify.R;
import com.stilldre.moovify.activity.MainActivity;
import com.stilldre.moovify.activity.DetailActivity;
import com.stilldre.moovify.model.MovieResponse;
import com.stilldre.moovify.model.Movie;
import com.stilldre.moovify.service.network.api.TMDb;

import static com.stilldre.moovify.activity.DetailActivity.MOVIE_ID;

public class ReleaseTodayReminder extends BroadcastReceiver {

    private final int REQUEST_CODE_RELEASE = 12;
    public static String CHANNEL_ID = "ch_1";
    public static CharSequence CHANNEL_NAME = "release_today";
    private ArrayList<Movie> movieList = new ArrayList<>(), tmpMovies;
    private NotificationCompat.Builder mBuilder;

    public ReleaseTodayReminder() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }


    private void releaseTodayNotification(Context context) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            mBuilder.setChannelId(CHANNEL_ID);
            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel(channel);
            }
        }
        Intent intent;
        PendingIntent pendingIntent;

        int numMovies = 0;
        try {
            numMovies = ((movieList.size() > 0) ? movieList.size() : 0);
        } catch (Exception e) {
            Log.w("ERROR", e.getMessage());
        }

        String msg = "";

        if (numMovies == 0) {
            msg = "Tidak ada Movie rilis hari ini";
            intent = new Intent(context, MainActivity.class);
            // | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            //pendingIntent = PendingIntent.getActivity(context, REQUEST_CODE_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            pendingIntent = TaskStackBuilder.create(context)
                    .addNextIntent(intent)
                    .getPendingIntent(REQUEST_CODE_RELEASE, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentTitle(context.getString(R.string.release_today_reminder))
                    .setContentText(msg)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(largeIcon)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(sound)
                    .setAutoCancel(true);
            if (mNotificationManager != null) {
                mNotificationManager.notify(0, mBuilder.build());
            }
        } else {
            intent = new Intent(context, DetailActivity.class);
            //| Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            for (int i = 0; i < numMovies; i++) {
                msg = movieList.get(i).getTitle() + " " + context.getString(R.string.release_today_reminder_msg);
                intent.putExtra(MOVIE_ID, movieList.get(i).getId());
                //pendingIntent = PendingIntent.getActivity(context, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                pendingIntent = TaskStackBuilder.create(context)
                        .addNextIntent(intent)
                        .getPendingIntent(i, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder.setContentTitle(context.getString(R.string.release_today_reminder))
                        .setContentText(msg)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(largeIcon)
                        .setContentIntent(pendingIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setSound(sound)
                        .setAutoCancel(true);
                if (mNotificationManager != null) {
                    mNotificationManager.notify(i, mBuilder.build());
                }
            }
        }
    }

    public void startReminder(Context context, String time) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseTodayReminder.class);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void stopReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_RELEASE, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

}

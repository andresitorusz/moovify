package com.stilldre.moovify.reminder;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.stilldre.moovify.BuildConfig;
import com.stilldre.moovify.R;
import com.stilldre.moovify.activity.MainActivity;
import com.stilldre.moovify.helper.MoovifyPreference;
import com.stilldre.moovify.model.Movie;
import com.stilldre.moovify.repository.MovieRepository;
import com.stilldre.moovify.service.network.callback.OnGetMoviesCallback;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

public class ReminderReceiver extends BroadcastReceiver {

    public final int REQUEST_CODE_DAILY = 28;
    public static String CHANNEL_ID = "01";
    public static CharSequence CHANNEL_NAME = "DAILY";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_MESSAGE = "message";
    public static final String TYPE_REPEATING_DAILY_REMINDER = "DailyReminder";
    public static final String TYPE_REPEATING_RELEASE_TODAY = "ReleaseToday";
    public static final int NOTIFICATION_ID_REPEATING_DAILY_REMINDER = 100;
    public static final int NOTIFICATION_ID_REPEATING_RELEASE_TODAY = 101;
    public MovieRepository movieRepository;

    private Context context;

    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        movieRepository = MovieRepository.getInstance();
        this.context = context;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String today = dateFormat.format(calendar.getTime());

        if (intent.getStringExtra(EXTRA_TYPE) != null && intent.getStringExtra(EXTRA_MESSAGE) != null) {
            if (intent.getStringExtra(EXTRA_TYPE).equals(TYPE_REPEATING_DAILY_REMINDER)) {
                showReminderNotification(context, context.getString(R.string.daily_reminder), context.getString(R.string.daily_reminder_msg), CHANNEL_ID);
            } else {
                movieRepository.getNowPlayingMovie(new OnGetMoviesCallback() {
                    @Override
                    public void onSuccess(ArrayList<Movie> movies) {
                        for (Movie m : movies) {
                            if (TextUtils.equals(m.getReleaseDate(), today)) {
                                showReminderNotification(context, context.getString(R.string.released_today) + m.getTitle(), m.getOverview(), m.getId());
                            }
                        }
                    }

                    @Override
                    public void onError() {
                    }
                });
            }
        } else {
            MoovifyPreference moovifyPreference = new MoovifyPreference(context);
            String intentAction = intent.getAction();
            if (intentAction != null && intentAction.equals(ACTION_BOOT_COMPLETED)) {
                if (moovifyPreference.getDailyReminder()) {
                    setRepeatingReminder(context, ReminderReceiver.TYPE_REPEATING_DAILY_REMINDER
                            , BuildConfig.TIME_DAILY, context.getString(R.string.daily_reminder_msg), ReminderReceiver.NOTIFICATION_ID_REPEATING_DAILY_REMINDER);
                }

                if (moovifyPreference.getReleaseTodayReminder()) {
                    setRepeatingReminder(context, ReminderReceiver.TYPE_REPEATING_RELEASE_TODAY
                            , BuildConfig.TIME_RELEASE_TODAY, context.getString(R.string.today), ReminderReceiver.NOTIFICATION_ID_REPEATING_RELEASE_TODAY);
                }
            }
        }
    }

    private void showReminderNotification(Context context, String title, String msg, String notifId) {

        Log.e(title, msg + " " + notifId);
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntent(intent)
                .getPendingIntent(REQUEST_CODE_DAILY, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notifId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(msg)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(sound)
                .setAutoCancel(true);

        if (notificationManager != null) {
            notificationManager.notify(Integer.parseInt(notifId), builder.build());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(notifId, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(notifId);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    public void setRepeatingReminder(Context context, String type, String time, String msg, int requestCode) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, msg);
        intent.putExtra(EXTRA_TYPE, type);
        String timeArray[] = time.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.e("TYPE: ", String.valueOf(requestCode));
    }

    public void stopReminder(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE_DAILY, intent, 0);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
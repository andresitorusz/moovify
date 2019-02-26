package com.stilldre.moovify.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MoovifyPreference {
    SharedPreferences sharedPreferences;
    Context context;

    public static final String PREFERENCES_REMINDER_DAILY = "pref_reminder_daily";
    public static final String PREFERENCES_REMINDER_DAILY_TIME = "pref_reminder_daily_time";
    public static final String PREFERENCES_REMINDER_RELEASE = "pref_reminder_release";

    public MoovifyPreference(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setDailyReminder(Boolean input, String time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFERENCES_REMINDER_DAILY, input);
        editor.putString(PREFERENCES_REMINDER_DAILY_TIME, time);
        editor.apply();
    }

    public Boolean getDailyReminder() {
        return sharedPreferences.getBoolean(PREFERENCES_REMINDER_DAILY, true);
    }

    public void setReleaseTodayReminder(Boolean input, String time) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFERENCES_REMINDER_RELEASE, input);
        editor.putString(PREFERENCES_REMINDER_DAILY_TIME, time);
        editor.apply();
    }

    public Boolean getReleaseTodayReminder() {
        return sharedPreferences.getBoolean(PREFERENCES_REMINDER_RELEASE, true);
    }
}
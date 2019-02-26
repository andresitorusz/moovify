package com.stilldre.moovify.activity;

import android.content.res.Configuration;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.stilldre.moovify.BuildConfig;
import com.stilldre.moovify.R;
import com.stilldre.moovify.helper.MoovifyPreference;
import com.stilldre.moovify.reminder.ReminderReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderActivity extends AppCompatActivity {

    public static final String TIME_DAILY = BuildConfig.TIME_DAILY;
    public static final String TIME_RELEASE = BuildConfig.TIME_RELEASE_TODAY;

    private ReminderReceiver reminderReceiver;
    private MoovifyPreference moovifyPreference;

    @BindView(R.id.tb_daily_reminder)
    ToggleButton tbDailyReminder;
    @BindView(R.id.tb_release_today_reminder)
    ToggleButton tbReleaseTodayReminder;
    @BindView(R.id.toolbar_reminder)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        ButterKnife.bind(this);

        toolbar.setTitle(getResources().getString(R.string.set_reminder));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(true);
        reminderReceiver = new ReminderReceiver();

        moovifyPreference = new MoovifyPreference(this);
        loadPreference();

        tbDailyReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                moovifyPreference.setDailyReminder(true, TIME_DAILY);
                reminderReceiver.setRepeatingReminder(this,
                        ReminderReceiver.TYPE_REPEATING_DAILY_REMINDER, TIME_DAILY,
                        getString(R.string.moovify_is_missing_you),
                        ReminderReceiver.NOTIFICATION_ID_REPEATING_DAILY_REMINDER);

                Toast.makeText(this, getString(R.string.enabled), Toast.LENGTH_SHORT).show();
            } else {
                moovifyPreference.setReleaseTodayReminder(false, TIME_DAILY);
                reminderReceiver.stopReminder(this);
                Toast.makeText(this, getString(R.string.disabled), Toast.LENGTH_SHORT).show();
            }
        });

        tbDailyReminder.setChecked(moovifyPreference.getDailyReminder());
        tbReleaseTodayReminder.setChecked(moovifyPreference.getReleaseTodayReminder());

        tbReleaseTodayReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                moovifyPreference.setReleaseTodayReminder(true, TIME_RELEASE);
                reminderReceiver.setRepeatingReminder(this,
                        ReminderReceiver.TYPE_REPEATING_RELEASE_TODAY, TIME_RELEASE,
                        getString(R.string.today),
                        ReminderReceiver.NOTIFICATION_ID_REPEATING_RELEASE_TODAY);

                Toast.makeText(this, getString(R.string.enabled), Toast.LENGTH_SHORT).show();
            } else {
                moovifyPreference.setReleaseTodayReminder(false, TIME_RELEASE);
                Toast.makeText(this, getString(R.string.disabled), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    private void loadPreference() {
        tbDailyReminder.setChecked(moovifyPreference.getDailyReminder());
        if (moovifyPreference.getDailyReminder()) {
            tbDailyReminder.setChecked(true);
        } else {
            tbDailyReminder.setChecked(false);
        }
    }
}

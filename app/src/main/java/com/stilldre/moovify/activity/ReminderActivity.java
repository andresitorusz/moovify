package com.stilldre.moovify.activity;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ToggleButton;

import com.stilldre.moovify.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderActivity extends AppCompatActivity {

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

        tbDailyReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tbDailyReminder.setChecked(true);
            } else {
                tbDailyReminder.setChecked(false);
            }
        });

        tbReleaseTodayReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tbReleaseTodayReminder.setChecked(true);
            } else {
                tbReleaseTodayReminder.setChecked(false);
            }
        });
    }
}

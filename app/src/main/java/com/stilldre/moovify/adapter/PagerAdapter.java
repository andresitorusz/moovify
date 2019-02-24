package com.stilldre.moovify.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stilldre.moovify.fragment.FavoriteFragment;
import com.stilldre.moovify.fragment.NowPlayingFragment;
import com.stilldre.moovify.fragment.UpcomingFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    int numOfTab;

    public PagerAdapter(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab = numOfTab;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new NowPlayingFragment();
            case 1:
                return new UpcomingFragment();
            case 2:
                return new FavoriteFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Now Playing";
            case 1:
                return "Upcoming";
            case 2:
                return "Favorite";
        }
        return null;
    }
}

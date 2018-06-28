package com.slidely.photoframe.repository;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.slidely.photoframe.R;
import com.slidely.photoframe.features.preferences.PreferencesFragment;

import java.util.concurrent.TimeUnit;

public class DefaultPreferenceRepository implements PreferenceRepository {
    private final Activity activity;

    DefaultPreferenceRepository(Activity activity) {
        this.activity = activity;
    }

    public int get() {
        SharedPreferences speedPreference = PreferenceManager.getDefaultSharedPreferences(activity);
        int defSpeedValue = activity.getResources().getInteger(R.integer.speed_default);
        return (int) (Integer.parseInt(speedPreference.getString(PreferencesFragment.SPEED_KEY,
                "" + defSpeedValue)) * TimeUnit.SECONDS.toMillis(1));
    }
}
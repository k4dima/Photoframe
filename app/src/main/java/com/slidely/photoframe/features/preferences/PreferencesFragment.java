package com.slidely.photoframe.features.preferences;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.slidely.photoframe.R;

public class PreferencesFragment extends PreferenceFragmentCompat {
    public static final String SPEED_KEY = "speed";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //noinspection ConstantConditions
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.preferences);
        EditTextPreference speed = (EditTextPreference) findPreference(SPEED_KEY);
        speed.setSummary(speed.getText());
        speed.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary((CharSequence) newValue);
            return true;
        });
    }
}
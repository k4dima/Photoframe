package com.slidely.photoframe.features.preferences;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import com.slidely.photoframe.R;

public class PreferencesFragment extends PreferenceFragment {
    public static final String SPEED_KEY = "speed";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
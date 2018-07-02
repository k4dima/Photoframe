package com.slidely.photoframe.features.preferences;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

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
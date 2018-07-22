package com.k4dima.photoframe.features.preferences

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import com.k4dima.photoframe.R
import com.k4dima.photoframe.features.main.data.DefaultPreferenceRepository

class PreferencesFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        addPreferencesFromResource(R.xml.preferences)
        (findPreference(DefaultPreferenceRepository.speedKey(context!!)) as EditTextPreference).apply {

            val seconds = " seconds"
            summary = text + seconds
            setOnPreferenceChangeListener { preference, newValue ->
                preference.summary = newValue as String + seconds
                true
            }
        }
    }
}
package com.slidely.photoframe.features.preferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.slidely.photoframe.R;
import com.slidely.photoframe.features.Utils;

public class PreferencesActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, new PreferencesFragment())
                .commitAllowingStateLoss();
        Utils.hideBars(this);
    }
}
package com.slidely.photoframe.repository;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

public final class RepositoryProvider {
    private static MediaRepository mediaRepository;
    private static PreferenceRepository preferenceRepository;

    public static MediaRepository provideMediaRepository(AppCompatActivity activity) {
        if (mediaRepository == null)
            mediaRepository = new DefaultMediaRepository(activity);
        return mediaRepository;
    }

    public static PreferenceRepository providePreferenceRepository(Activity activity) {
        if (preferenceRepository == null)
            preferenceRepository = new DefaultPreferenceRepository(activity);
        return preferenceRepository;
    }
}
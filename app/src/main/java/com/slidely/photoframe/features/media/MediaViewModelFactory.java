package com.slidely.photoframe.features.media;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.slidely.photoframe.repository.MediaRepository;
import com.slidely.photoframe.repository.PreferenceRepository;

public class MediaViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MediaRepository mediaRepository;
    private final MediaView mediaView;
    private final PreferenceRepository preferenceRepository;

    MediaViewModelFactory(MediaView mediaView, MediaRepository mediaRepository,
                          PreferenceRepository preferenceRepository) {
        this.mediaView = mediaView;
        this.mediaRepository = mediaRepository;
        this.preferenceRepository = preferenceRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MediaViewModel(mediaView, mediaRepository, preferenceRepository);
    }
}

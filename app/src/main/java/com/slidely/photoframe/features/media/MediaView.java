package com.slidely.photoframe.features.media;

import androidx.lifecycle.LifecycleOwner;

import com.slidely.photoframe.model.Media;

public interface MediaView extends LifecycleOwner {
    void showMedia(Media media);

    void postDelayed(Runnable r, long delayMillis);

    void removeCallbacks(Runnable r);
}
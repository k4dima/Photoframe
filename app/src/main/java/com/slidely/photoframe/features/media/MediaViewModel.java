package com.slidely.photoframe.features.media;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.annotation.Nullable;

import com.slidely.photoframe.model.Media;
import com.slidely.photoframe.repository.MediaRepository;
import com.slidely.photoframe.repository.PreferenceRepository;

import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

class MediaViewModel extends ViewModel {
    private final MediaRepository mediaRepository;
    private final MediaView mediaView;
    private int slideshowSpeed;
    private MediaAdapter mediaAdapter = new MediaAdapter();
    private Runnable runnable = new Runnable() {
        public void run() {
            mediaView.showMedia(mediaAdapter.getItem());
            mediaView.postDelayed(this, slideshowSpeed);
        }
    };

    MediaViewModel(@NotNull MediaView mediaView, @NotNull MediaRepository mediaRepository,
                   PreferenceRepository preferenceRepository) {
        this.mediaView = mediaView;
        this.mediaRepository = mediaRepository;
        mediaRepository.media(0, result -> {
            mediaAdapter.submitList(result);
            mediaView.showMedia(mediaAdapter.getItem());
        });
        mediaView.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            public void start() {
                slideshowSpeed = preferenceRepository.get();
                mediaView.postDelayed(runnable, slideshowSpeed);
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            public void stop() {
                mediaView.removeCallbacks(runnable);
            }
        });
    }

    class MediaAdapter {
        private static final int MIN_SAFE_NUMBER = 10;
        private Deque<Media> deque = new LinkedList<>();
        private int currentPosition;
        private int total;

        @Nullable
        Media getItem() {
            if (deque.size() == MIN_SAFE_NUMBER) {
                final int offset = currentPosition + MIN_SAFE_NUMBER;
                mediaRepository.media(offset, result -> {
                    if (result.size() == 0) {
                        total = offset;
                        mediaRepository.media(0, this::submitList);
                    }
                    submitList(result);
                });
            }
            Media media = deque.isEmpty() ? null : deque.removeFirst();
            if (total != 0 && currentPosition == total - 1) {
                currentPosition = 0;
            } else {
                currentPosition++;
            }
            return media;
        }

        void submitList(List<Media> list) {
            this.deque.addAll(list);
        }
    }
}
package com.slidely.photoframe.repository;

import com.slidely.photoframe.model.Media;

import java.util.List;

public interface MediaRepository {
    void media(int offset, CompletedListener<List<Media>> completedListener);

    interface CompletedListener<T> {
        void completed(T result);
    }
}
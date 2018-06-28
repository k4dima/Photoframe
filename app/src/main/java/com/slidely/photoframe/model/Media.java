package com.slidely.photoframe.model;

public class Media {
    private final String uri;
    private final boolean video;

    public Media(String uri, boolean video) {
        this.uri = uri;
        this.video = video;
    }

    public String getUri() {
        return uri;
    }

    public boolean isVideo() {
        return video;
    }
}
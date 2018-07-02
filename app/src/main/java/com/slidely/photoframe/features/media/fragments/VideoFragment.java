package com.slidely.photoframe.features.media.fragments;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slidely.photoframe.R;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

import static com.slidely.photoframe.features.media.MainActivity.URI_KEY;

public class VideoFragment extends Fragment {
    private ScalableVideoView videoView;
    private boolean prepared;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //noinspection ConstantConditions
        Uri uri = Uri.parse(getArguments().getString(URI_KEY));
        videoView = (ScalableVideoView) getView();
        try {
            //noinspection ConstantConditions
            videoView.setDataSource(getContext(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (videoView != null)
                startVideo();
        } else {
            stopVideo();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getUserVisibleHint())
            startVideo();
    }

    @Override
    public void onStop() {
        stopVideo();
        super.onStop();
    }

    private void startVideo() {
        videoView.prepareAsync(mp -> {
            prepared = true;
            mp.start();
        });
    }

    private void stopVideo() {
        if (prepared)
            videoView.stop();
    }
}
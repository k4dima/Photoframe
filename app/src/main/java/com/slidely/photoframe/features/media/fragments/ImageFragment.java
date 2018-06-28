package com.slidely.photoframe.features.media.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.slidely.photoframe.R;

import static com.slidely.photoframe.features.media.MediaActivity.URI_KEY;

public class ImageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //noinspection ConstantConditions
        Uri uri = Uri.parse(getArguments().getString(URI_KEY));
        ImageView imageView = (ImageView) getView();
        //noinspection ConstantConditions
        Glide.with(this)
                .load(uri)
                .into(imageView);
    }
}
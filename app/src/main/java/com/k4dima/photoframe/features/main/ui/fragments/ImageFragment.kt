package com.k4dima.photoframe.features.main.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.k4dima.photoframe.R
import com.k4dima.photoframe.features.main.ui.MainActivity.Companion.URI_KEY

class ImageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.image, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val uri = Uri.parse(arguments!!.getString(URI_KEY))
        val imageView = getView() as ImageView
        Glide.with(view)
                .load(uri)
                .into(imageView)
    }
}
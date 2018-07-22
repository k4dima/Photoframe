package com.k4dima.photoframe.features.main.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.k4dima.photoframe.R
import com.k4dima.photoframe.features.main.data.PreferenceRepository
import com.k4dima.photoframe.features.main.ui.MainActivity.Companion.URI_KEY
import com.yqritc.scalablevideoview.ScalableVideoView
import dagger.android.support.DaggerFragment
import io.reactivex.functions.Consumer
import java.io.IOException
import javax.inject.Inject

class VideoFragment : DaggerFragment() {
    @Inject
    lateinit var preferences: PreferenceRepository
    private var videoView: ScalableVideoView? = null
    private var prepared: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.video, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val uri = Uri.parse(arguments!!.getString(URI_KEY))
        videoView = getView() as ScalableVideoView
        try {
            videoView!!.setDataSource(context!!, uri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        preferences.sound.subscribe(Consumer {
            val volume = (if (it) 1 else 0).toFloat()
            videoView!!.setVolume(volume, volume)
        })
                .run {
                    lifecycle.addObserver(object : LifecycleObserver {
                        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                        fun unsubscribe() = dispose()
                    })
                }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (videoView != null)
                startVideo()
        } else {
            stopVideo()
        }
    }

    override fun onStart() {
        super.onStart()
        if (userVisibleHint)
            startVideo()
    }

    override fun onStop() {
        stopVideo()
        super.onStop()
    }

    private fun startVideo() {
        videoView!!.prepareAsync { mp ->
            prepared = true
            mp.start()
        }
    }

    private fun stopVideo() {
        if (prepared)
            videoView!!.stop()
    }
}
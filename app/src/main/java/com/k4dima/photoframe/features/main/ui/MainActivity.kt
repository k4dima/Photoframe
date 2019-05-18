package com.k4dima.photoframe.features.main.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.transition.TransitionManager
import android.view.*
import android.view.View.*
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import androidx.lifecycle.*
import com.k4dima.photoframe.R
import com.k4dima.photoframe.R.*
import com.k4dima.photoframe.features.main.data.PreferenceRepository
import com.k4dima.photoframe.features.main.data.model.Media
import com.k4dima.photoframe.features.main.presentation.MediaViewModel
import com.k4dima.photoframe.features.main.ui.fragments.ImageFragment
import com.k4dima.photoframe.features.main.ui.fragments.VideoFragment
import com.k4dima.photoframe.features.preferences.PreferencesActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tomerrosenfeld.customanalogclockview.CustomAnalogClock
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var preferences: PreferenceRepository
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var fragment: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        fragment = findViewById<View>(id.media_fragment)
        fragment.setOnTouchListener { _, _ ->
            fullscreen(supportActionBar!!.isShowing)
            false
        }
        val compositeDisposable = CompositeDisposable().apply {
            lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun unsubscribe() = clear()
            })
        }
        preferences.fullscreen.subscribe(Consumer {
            fullscreen(it)
        })
                .addTo(compositeDisposable)
        preferences.clock.observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    findViewById<CustomAnalogClock>(id.analog_clock).apply {
                        visibility = if (it) VISIBLE else GONE
                        setAutoUpdate(it)
                        if (it) {
                            init(this@MainActivity, drawable.watch_face, drawable.hour_hand,
                                    drawable.minute_hand, 0, false, false)
                            setOnClickListener(object : OnClickListener {
                                private val map = mapOf(
                                        0 to (Gravity.BOTTOM or Gravity.START),
                                        1 to (Gravity.TOP or Gravity.START),
                                        2 to (Gravity.TOP or Gravity.END),
                                        3 to Gravity.CENTER,
                                        4 to (Gravity.BOTTOM or Gravity.END)
                                )
                                private var clockPosition = 0

                                override fun onClick(view: View) {
                                    TransitionManager.beginDelayedTransition(view.rootView as ViewGroup)
                                    val layoutParams = view.layoutParams as FrameLayout.LayoutParams
                                    layoutParams.gravity = map[clockPosition]!!
                                    clockPosition = if (clockPosition == 4) 0 else clockPosition + 1
                                    view.layoutParams = layoutParams
                                }
                            })
                        }
                    }
                }
                .addTo(compositeDisposable)
    }

    override fun onStart() {
        super.onStart()
        RxPermissions(this@MainActivity).apply {
            if (isGranted(READ_EXTERNAL_STORAGE))
                startSlideShow()
            else
                requestPermission(this)
        }
    }

    private fun requestPermission(permission: RxPermissions) {
        permission.requestEach(READ_EXTERNAL_STORAGE)
                .subscribe {
                    if (it.granted)
                        startSlideShow()
                    else
                        AlertDialog.Builder(this)
                                .setMessage(string.permission)
                                .setPositiveButton(string.allow_access) { _, _ ->
                                    if (it.shouldShowRequestPermissionRationale)
                                        requestPermission(permission)
                                    else
                                        startActivity(Intent().apply {
                                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                            data = Uri.fromParts("package", packageName, null)
                                        })
                                }
                                .setCancelable(false)
                                .show().apply {
                                    lifecycle.addObserver(object : LifecycleObserver {
                                        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
                                        fun stop() = dismiss()
                                    })
                                }
                }
                .run {
                    lifecycle.addObserver(object : LifecycleObserver {
                        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
                        fun unsubscribe() = dispose()
                    })
                }
    }

    private fun fullscreen(fullscreen: Boolean) =
            if (fullscreen)
                fragment.systemUiVisibility =
                        SYSTEM_UI_FLAG_LOW_PROFILE or
                        SYSTEM_UI_FLAG_FULLSCREEN or
                        SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        SYSTEM_UI_FLAG_HIDE_NAVIGATION
            else
                fragment.systemUiVisibility =
                        SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            if (item.itemId == id.menu_settings) {
                startActivity(Intent(this, PreferencesActivity::class.java))
                true
            } else {
                false
            }

    private fun startSlideShow() {
        ViewModelProviders.of(this, viewModelFactory)
                .get(MediaViewModel::class.java)
                .media
                .observe(this, Observer(::showMedia))
    }

    private fun showMedia(media: Media?) =
            if (media != null)
                supportFragmentManager.transaction {
                    setCustomAnimations(anim.fade_in, anim.fade_out)
                            .replace(id.media_fragment,
                                    Fragment.instantiate(this@MainActivity,
                                            if (media.isVideo)
                                                VideoFragment::class.java.name
                                            else
                                                ImageFragment::class.java.name,
                                            bundleOf(URI_KEY to media.uri)),
                                    null)
                }
            else
                findViewById<View>(id.empty).visibility = VISIBLE

    companion object {
        const val URI_KEY = "uri"
    }
}
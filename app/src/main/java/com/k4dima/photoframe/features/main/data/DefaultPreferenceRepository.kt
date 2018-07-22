package com.k4dima.photoframe.features.main.data

import android.content.Context
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.k4dima.photoframe.R
import com.k4dima.photoframe.features.main.ui.di.MainScope
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@MainScope
class DefaultPreferenceRepository
@Inject
internal constructor(private val context: Context) : PreferenceRepository {
    companion object {
        internal fun speedKey(context: Context) = context.getString(R.string.speed_key)!!
    }

    private val preferencesSingle = Single.fromCallable { PreferenceManager.getDefaultSharedPreferences(context) }
            .subscribeOn(Schedulers.io())
    /** Keep a strong reference on your RxSharedPreferences instance for as long as you want to observe them
     * to prevent listeners from being GCed.*/
    private lateinit var rxPreferences: RxSharedPreferences
    private var rxPreferencesSingle = preferencesSingle.map {
        RxSharedPreferences.create(it).apply { rxPreferences = this }
    }!!
    override val fullscreen = preferencesSingle
            .map { it.getBoolean(context.getString(R.string.fullscreen_key), true) }!!
    override val clock = rxPreferencesSingle.map { it.getBoolean(context.getString(R.string.clock_key), false) }
            .toObservable()
            .flatMap { it.asObservable() }!!
    override val sound = preferencesSingle.map { it.getBoolean(context.getString(R.string.sound_key), false) }!!
    /**In milliseconds*/
    override val speed: Single<Int> = preferencesSingle.map {
        val defaultSpeedValue = "" + context.resources.getInteger(R.integer.speed_default)
        (Integer.parseInt(it.getString(speedKey(context),
                defaultSpeedValue)) * TimeUnit.SECONDS.toMillis(1)).toInt()
    }
}
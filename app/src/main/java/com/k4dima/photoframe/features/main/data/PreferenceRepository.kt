package com.k4dima.photoframe.features.main.data

import io.reactivex.Observable
import io.reactivex.Single

interface PreferenceRepository {
    val fullscreen: Single<Boolean>
    val clock: Observable<Boolean>
    val sound: Single<Boolean>
    val speed: Single<Int>
}
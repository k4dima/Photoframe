package com.k4dima.photoframe.features.main.data

import com.k4dima.photoframe.features.main.data.model.Media
import io.reactivex.Single

interface MediaRepository {
    fun media(offset: Int): Single<List<Media>>
}
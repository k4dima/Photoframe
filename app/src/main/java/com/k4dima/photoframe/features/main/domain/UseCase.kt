package com.k4dima.photoframe.features.main.domain

import androidx.lifecycle.LiveData
import com.k4dima.photoframe.features.main.data.model.Media

interface UseCase {
    val mediaLiveData: LiveData<Media>
}
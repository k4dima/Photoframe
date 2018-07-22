package com.k4dima.photoframe.features.main.presentation

import androidx.lifecycle.ViewModel
import com.k4dima.photoframe.features.main.domain.UseCase
import com.k4dima.photoframe.features.main.ui.di.MainScope
import javax.inject.Inject

@MainScope
internal class MediaViewModel
@Inject
constructor(useCase: UseCase) : ViewModel() {
    val media = useCase.mediaLiveData
}
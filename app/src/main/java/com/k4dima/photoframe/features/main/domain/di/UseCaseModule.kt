package com.k4dima.photoframe.features.main.domain.di

import com.k4dima.photoframe.features.main.domain.MediaUseCase
import com.k4dima.photoframe.features.main.domain.UseCase
import com.k4dima.photoframe.features.main.ui.di.MainScope
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {
    @MainScope
    @Binds
    fun useCase(mediaUseCase: MediaUseCase): UseCase
}
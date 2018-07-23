package com.k4dima.photoframe.features.main.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.k4dima.photoframe.features.main.presentation.MediaViewModel
import com.k4dima.photoframe.features.main.presentation.MediaViewModelFactory
import com.k4dima.photoframe.features.main.ui.di.MainScope
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MediaViewModel::class)
    internal abstract fun bindMediaViewModel(repoViewModel: MediaViewModel): ViewModel

    @MainScope
    @Binds
    internal abstract fun bindViewModelFactory(factory: MediaViewModelFactory): ViewModelProvider.Factory
}
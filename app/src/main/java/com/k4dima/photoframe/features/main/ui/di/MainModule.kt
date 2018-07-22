package com.k4dima.photoframe.features.main.ui.di

import com.k4dima.photoframe.features.main.ui.fragments.VideoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainModule {
    @ContributesAndroidInjector
    fun videoFragment(): VideoFragment
}
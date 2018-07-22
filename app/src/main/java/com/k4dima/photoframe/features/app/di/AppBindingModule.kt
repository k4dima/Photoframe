package com.k4dima.photoframe.features.app.di

import com.k4dima.photoframe.features.main.data.di.RepositoryProvider
import com.k4dima.photoframe.features.main.domain.di.UseCaseModule
import com.k4dima.photoframe.features.main.presentation.di.ViewModelModule
import com.k4dima.photoframe.features.main.ui.MainActivity
import com.k4dima.photoframe.features.main.ui.di.MainModule
import com.k4dima.photoframe.features.main.ui.di.MainScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface AppBindingModule {
    @MainScope
    @ContributesAndroidInjector(modules = [RepositoryProvider::class,
        MainModule::class,
        ViewModelModule::class,
        UseCaseModule::class])
    fun mainActivity(): MainActivity
}
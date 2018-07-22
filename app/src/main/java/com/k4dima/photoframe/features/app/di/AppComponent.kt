package com.k4dima.photoframe.features.app.di

import com.k4dima.photoframe.features.app.App
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppBindingModule::class, AndroidSupportInjectionModule::class])
interface AppComponent : AndroidInjector<App>
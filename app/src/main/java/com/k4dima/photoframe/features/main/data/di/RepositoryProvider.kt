package com.k4dima.photoframe.features.main.data.di

import android.content.Context
import com.k4dima.photoframe.features.main.data.DefaultMediaRepository
import com.k4dima.photoframe.features.main.data.DefaultPreferenceRepository
import com.k4dima.photoframe.features.main.data.MediaRepository
import com.k4dima.photoframe.features.main.data.PreferenceRepository
import com.k4dima.photoframe.features.main.ui.MainActivity
import com.k4dima.photoframe.features.main.ui.di.MainScope
import com.squareup.sqlbrite3.SqlBrite
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RepositoryProvider {
    @Module
    companion object {
        @MainScope
        @JvmStatic
        @Provides
        fun sqlBrite() = SqlBrite.Builder().build()

        @JvmStatic
        @Provides
        fun contentResolver(mainActivity: MainActivity) = mainActivity.contentResolver!!

        @JvmStatic
        @Provides
        fun context(mainActivity: MainActivity): Context = mainActivity.applicationContext
    }

    @MainScope
    @Binds
    abstract fun provideMediaRepository(defaultMediaRepository: DefaultMediaRepository): MediaRepository

    @MainScope
    @Binds
    abstract fun providePreferenceRepository(defaultPreferenceRepository: DefaultPreferenceRepository): PreferenceRepository
}
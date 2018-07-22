package com.k4dima.photoframe.features.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class MediaViewModelFactory
@Inject
internal constructor(private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            creators[modelClass]!!.get() as T
}
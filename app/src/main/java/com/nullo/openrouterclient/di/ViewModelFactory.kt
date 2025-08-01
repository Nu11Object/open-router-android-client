package com.nullo.openrouterclient.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@ApplicationScope
@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val viewModelProviders: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = viewModelProviders[modelClass] ?: throw IllegalArgumentException(
            "Unknown model class: $modelClass. Known are: ${viewModelProviders.keys}"
        )
        return provider.get() as T
    }
}

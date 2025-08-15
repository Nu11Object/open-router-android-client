package com.nullo.openrouterclient.di

import android.app.Application
import com.nullo.openrouterclient.di.modules.CoroutineScopeModule
import com.nullo.openrouterclient.di.modules.DataModule
import com.nullo.openrouterclient.di.modules.DatabaseModule
import com.nullo.openrouterclient.di.modules.MarkwonModule
import com.nullo.openrouterclient.di.modules.NetworkModule
import com.nullo.openrouterclient.di.modules.SharedPreferencesModule
import com.nullo.openrouterclient.di.modules.ViewModelModule
import com.nullo.openrouterclient.presentation.MainActivity
import com.nullo.openrouterclient.presentation.OpenRouterClientApp
import com.nullo.openrouterclient.presentation.aimodels.SelectModelFragment
import com.nullo.openrouterclient.presentation.settings.SettingsFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        NetworkModule::class,
        DatabaseModule::class,
        SharedPreferencesModule::class,
        ViewModelModule::class,
        DataModule::class,
        MarkwonModule::class,
        CoroutineScopeModule::class
    ]
)
@ApplicationScope
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(selectModelFragment: SelectModelFragment)

    fun inject(settingsFragment: SettingsFragment)

    fun inject(openRouterClientApp: OpenRouterClientApp)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}

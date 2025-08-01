package com.nullo.openrouterclient.di.modules

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nullo.openrouterclient.di.ApplicationScope
import com.nullo.openrouterclient.di.qualifiers.ApiKeyQualifier
import com.nullo.openrouterclient.di.qualifiers.SettingsQualifier
import dagger.Module
import dagger.Provides

@Module
class SharedPreferencesModule {

    @Provides
    @ApplicationScope
    @SettingsQualifier
    fun provideSettingsSharedPreferences(
        application: Application
    ): SharedPreferences {
        return application.getSharedPreferences(NAME_SETTINGS_PREFS, MODE_PRIVATE)
    }

    @Provides
    @ApplicationScope
    @ApiKeyQualifier
    fun provideApiKeySharedPreferences(
        application: Application
    ): SharedPreferences {
        // Can be replaced with encryption feature soon
        return application.getSharedPreferences(NAME_APIKEY_PREFS, MODE_PRIVATE)
    }

    @Provides
    @ApplicationScope
    fun provideGson(): Gson {
        return Gson()
    }

    companion object {

        const val NAME_SETTINGS_PREFS = "settings_prefs"
        const val NAME_APIKEY_PREFS = "apikey_prefs"
    }
}

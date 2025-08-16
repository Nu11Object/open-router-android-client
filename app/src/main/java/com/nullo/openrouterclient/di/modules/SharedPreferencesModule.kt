package com.nullo.openrouterclient.di.modules

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nullo.openrouterclient.di.qualifiers.ApiKeyQualifier
import com.nullo.openrouterclient.di.qualifiers.SettingsQualifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SharedPreferencesModule {

    @Provides
    @Singleton
    @SettingsQualifier
    fun provideSettingsSharedPreferences(
        application: Application
    ): SharedPreferences {
        return application.getSharedPreferences(NAME_SETTINGS_PREFS, MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @ApiKeyQualifier
    fun provideApiKeySharedPreferences(
        application: Application
    ): SharedPreferences {
        // Can be replaced with encryption feature soon
        return application.getSharedPreferences(NAME_APIKEY_PREFS, MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    companion object {

        private const val NAME_SETTINGS_PREFS = "settings_prefs"
        private const val NAME_APIKEY_PREFS = "apikey_prefs"
    }
}

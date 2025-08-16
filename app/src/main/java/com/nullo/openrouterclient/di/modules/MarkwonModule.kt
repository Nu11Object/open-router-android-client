package com.nullo.openrouterclient.di.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.noties.markwon.Markwon
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MarkwonModule {

    @Provides
    @Singleton
    fun provideMarkwon(application: Application): Markwon {
        return Markwon.create(application)
    }
}

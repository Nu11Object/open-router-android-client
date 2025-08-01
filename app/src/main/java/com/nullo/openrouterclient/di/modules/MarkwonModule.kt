package com.nullo.openrouterclient.di.modules

import android.app.Application
import com.nullo.openrouterclient.di.ApplicationScope
import dagger.Module
import dagger.Provides
import io.noties.markwon.Markwon

@Module
class MarkwonModule {

    @Provides
    @ApplicationScope
    fun provideMarkwon(application: Application): Markwon {
        return Markwon.create(application)
    }
}

package com.nullo.openrouterclient.di.modules

import android.app.Application
import androidx.room.Room
import com.nullo.openrouterclient.data.database.AppDatabase
import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.database.chat.ChatDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAiModelsDao(database: AppDatabase): AiModelsDao {
        return database.aiModelsDao()
    }

    @Provides
    @Singleton
    fun provideChatDao(database: AppDatabase): ChatDao {
        return database.chatDao()
    }

    companion object {
        private const val DATABASE_NAME = "openrouter_client.db"
    }
}

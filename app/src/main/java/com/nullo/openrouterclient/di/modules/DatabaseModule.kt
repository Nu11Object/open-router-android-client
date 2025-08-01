package com.nullo.openrouterclient.di.modules

import android.app.Application
import androidx.room.Room
import com.nullo.openrouterclient.data.database.AppDatabase
import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.database.chat.ChatDao
import com.nullo.openrouterclient.di.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    private val databaseName = "AiModels.db"

    @Provides
    @ApplicationScope
    fun provideAiModelsDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            databaseName
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideAiModelsDao(database: AppDatabase): AiModelsDao {
        return database.aiModelsDao()
    }

    @Provides
    @ApplicationScope
    fun provideChatDao(database: AppDatabase): ChatDao {
        return database.chatDao()
    }
}

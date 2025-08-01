package com.nullo.openrouterclient.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nullo.openrouterclient.data.database.aiModels.AiModelDbEntity
import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.database.chat.ChatDao
import com.nullo.openrouterclient.data.database.chat.MessageDbEntity

@Database(
    entities = [AiModelDbEntity::class, MessageDbEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun aiModelsDao(): AiModelsDao
    abstract fun chatDao(): ChatDao
}

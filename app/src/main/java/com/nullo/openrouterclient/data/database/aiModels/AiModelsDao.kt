package com.nullo.openrouterclient.data.database.aiModels

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AiModelsDao {

    @Query("SELECT * FROM ai_models")
    fun getModels(): LiveData<List<AiModelDbEntity>>

    @Query("SELECT * FROM ai_models LIMIT 1")
    suspend fun getDefaultViewModel(): AiModelDbEntity

    @Query("SELECT COUNT(*) FROM ai_models")
    suspend fun getModelsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModelList(models: List<AiModelDbEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModel(aiModelDbEntity: AiModelDbEntity)

    @Delete
    suspend fun removeModel(aiModelDbEntity: AiModelDbEntity)

    @Query("DELETE FROM ai_models WHERE id = :id")
    suspend fun removeModelById(id: Long): Int
}

package com.nullo.openrouterclient.presentation

import android.app.Application
import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.database.aiModels.AiModelsProvider
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidApp
class OpenRouterClientApp : Application() {

    @Inject
    lateinit var aiModelsDao: AiModelsDao

    @Inject
    lateinit var aiModelsProvider: AiModelsProvider

    override fun onCreate() {
        super.onCreate()
        initializeDatabase()
    }

    private fun initializeDatabase() {
        runBlocking {
            if (aiModelsDao.getModelsCount() == 0) {
                aiModelsDao.insertModelList(aiModelsProvider.defaultModels)
            }
        }
    }
}

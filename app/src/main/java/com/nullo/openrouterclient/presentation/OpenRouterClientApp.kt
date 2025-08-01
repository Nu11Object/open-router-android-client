package com.nullo.openrouterclient.presentation

import android.app.Application
import com.nullo.openrouterclient.data.database.aiModels.AiModelsDao
import com.nullo.openrouterclient.data.database.aiModels.AiModelsProvider
import com.nullo.openrouterclient.di.DaggerApplicationComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class OpenRouterClientApp : Application() {

    @Inject
    lateinit var aiModelsDao: AiModelsDao

    @Inject
    lateinit var aiModelsProvider: AiModelsProvider

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        component.inject(this)
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

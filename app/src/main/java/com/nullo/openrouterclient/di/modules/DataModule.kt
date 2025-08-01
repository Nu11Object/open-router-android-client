package com.nullo.openrouterclient.di.modules

import com.nullo.openrouterclient.data.repository.AiModelsRepositoryImpl
import com.nullo.openrouterclient.data.repository.ChatRepositoryImpl
import com.nullo.openrouterclient.data.repository.SettingsRepositoryImpl
import com.nullo.openrouterclient.di.ApplicationScope
import com.nullo.openrouterclient.domain.repositories.AiModelsRepository
import com.nullo.openrouterclient.domain.repositories.ChatRepository
import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @ApplicationScope
    fun bindAiModelsRepository(impl: AiModelsRepositoryImpl): AiModelsRepository

    @Binds
    @ApplicationScope
    fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}

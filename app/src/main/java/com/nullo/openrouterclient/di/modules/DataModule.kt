package com.nullo.openrouterclient.di.modules

import com.nullo.openrouterclient.data.repository.AiModelsRepositoryImpl
import com.nullo.openrouterclient.data.repository.ChatRepositoryImpl
import com.nullo.openrouterclient.data.repository.SettingsRepositoryImpl
import com.nullo.openrouterclient.domain.repositories.AiModelsRepository
import com.nullo.openrouterclient.domain.repositories.ChatRepository
import com.nullo.openrouterclient.domain.repositories.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    fun bindAiModelsRepository(impl: AiModelsRepositoryImpl): AiModelsRepository

    @Binds
    @Singleton
    fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository
}

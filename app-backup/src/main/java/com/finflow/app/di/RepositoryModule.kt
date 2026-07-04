package com.finflow.app.di

import com.finflow.app.data.repository.FlowRepository
import com.finflow.app.data.repository.FlowRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFlowRepository(impl: FlowRepositoryImpl): FlowRepository
}

package com.example.lloyddemoapplication.di

import com.example.lloyddemoapplication.data.remote.network.ApiService
import com.example.lloyddemoapplication.data.repository.UserRepositoryImpl
import com.example.lloyddemoapplication.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideUserRepository(apiService: ApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }
}
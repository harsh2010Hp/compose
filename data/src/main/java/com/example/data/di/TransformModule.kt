package com.example.data.di

import com.example.data.mapper.user.UserMapper
import com.example.data.mapper.userinfo.UserInfoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class TransformModule {
    @Provides
    fun provideUserMapper() = UserMapper()

    @Provides
    fun provideUserInfoMapper() = UserInfoMapper()
}
package com.example.domain.usecase.userinfo

import com.example.core.Response
import com.example.domain.model.UserInfo
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String?): Flow<Response<UserInfo>> =
        repository.getUserInfo(userId)
}
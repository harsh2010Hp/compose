package com.example.domain.usecase.userinfo

import com.example.core.Response
import com.example.domain.model.UserInfo
import com.example.domain.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class GetUserInfoUseCaseTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given users available, When getUserInfoUseCase invoked, Then return success response`() =
        runTest {
            // Given
            val users = UserInfo("user1")
            coEvery { userRepository.getUserInfo("123") } returns users

            val getUserInfoUseCase = GetUserInfoUseCase(userRepository, Dispatchers.Unconfined)

            // When
            val response = getUserInfoUseCase("123").toList()

            // Then
            assertEquals(Response.Success(users).data?.name, response.first().data?.name)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given error occurs, When getUserUseCase invoked, Then return error response`() =
        runTest {
            // Given
            val exception = RuntimeException("Error fetching users")
            coEvery { userRepository.getUserInfo("123") } throws exception

            val getUserInfoUseCase = GetUserInfoUseCase(userRepository, Dispatchers.Unconfined)

            // When
            val response = getUserInfoUseCase("123").toList()

            // Then
            assertEquals(
                Response.Error<RuntimeException>(exception).exception?.message,
                response.first().exception?.message
            )
        }
}
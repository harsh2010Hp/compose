package com.example.domain.usecase.user

import com.example.core.Response
import com.example.domain.model.User
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
class GetUserUseCaseTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given users available, When getUserUseCase invoked, Then return success response`() =
        runTest {
            // Given
            val users = listOf(User("user1"), User("user2"))
            coEvery { userRepository.getUsers() } returns users

            val getUserUseCase = GetUserUseCase(userRepository, Dispatchers.Unconfined)

            // When
            val response = getUserUseCase().toList()

            // Then
            assertEquals(Response.Success(users).data?.first(), response.first().data?.first())
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given error occurs, When getUserUseCase invoked, Then return error response`() =
        runTest {
            // Given
            val exception = RuntimeException("Error fetching users")
            coEvery { userRepository.getUsers() } throws exception

            val getUserUseCase = GetUserUseCase(userRepository, Dispatchers.Unconfined)

            // When
            val response = getUserUseCase().toList()

            // Then
            assertEquals(
                Response.Error<RuntimeException>(exception).exception?.message,
                response.first().exception?.message
            )
        }
}
package com.example.domain.usecase.userinfo

import com.example.core.Response
import com.example.domain.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class GetUserInfoUseCaseTest {

    @MockK
    private lateinit var userRepository: UserRepository

    private companion object {

        private const val TEST_USER_ID = "1"
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given query userId, When getUserInfoUseCase invoked, Then verify repository called`() =
        runTest {
            coEvery { userRepository.getUserInfo(TEST_USER_ID) } returns flowOf(
                Response.Loading(
                    true
                )
            )

            GetUserInfoUseCase(userRepository).invoke(TEST_USER_ID)
            coEvery {
                userRepository.getUserInfo(TEST_USER_ID)
            }
        }
}
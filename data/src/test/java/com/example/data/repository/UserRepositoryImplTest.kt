package com.example.data.repository

import com.example.core.Response
import com.example.data.mapper.user.UserMapper
import com.example.data.mapper.userinfo.UserInfoMapper
import com.example.domain.model.User
import com.example.domain.model.UserInfo
import com.example.domain.repository.UserRepository
import com.example.network.dto.UserDto
import com.example.network.dto.UserInfoDto
import com.example.network.service.ApiService
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class UserRepositoryImplTest {

    private lateinit var userRepository: UserRepository

    private lateinit var apiService: ApiService
    private lateinit var userMapper: UserMapper
    private lateinit var userInfoMapper: UserInfoMapper
    private lateinit var usersDto: List<UserDto>
    private lateinit var users: List<User>
    private lateinit var userInfo: UserInfo
    private lateinit var userInfoDto: UserInfoDto

    @Before
    fun setUp() {
        usersDto = mockk()
        apiService = mockk()
        userMapper = mockk()
        userInfoMapper = mockk()
        users = mockk()
        usersDto = mockk()
        userInfoDto = mockk()
        userInfo = mockk()
        userRepository =
            UserRepositoryImpl(apiService, userMapper = userMapper, userInfoMapper = userInfoMapper)
    }

    @Test
    fun `Given successful getUsers API call, When getUsers is invoked, Then it should return Success`() =
        runTest {
            coEvery { apiService.getUsers() } returns usersDto

            every { userMapper.map(usersDto) } returns users

            val result = userRepository.getUsers().first()
            assertTrue(result is Response.Success)
        }

    @Test
    fun `Given a network error in getUsers API call, When getUsers is invoked, Then it should handle the error`() =
        runTest {
            val expectedException = IOException(NETWORK_ERROR_MESSAGE)

            coEvery { apiService.getUsers() } throws expectedException

            val result = userRepository.getUsers().first()

            assertTrue(result is Response.Error)
        }

    @Test
    fun `Given successful getUserInfo API call, When getUserInfo is invoked, Then it should return Success`() =
        runTest {
            coEvery { apiService.getUserInfo(TEST_USER_ID) } returns userInfoDto
            every { userInfoMapper.map(userInfoDto) } returns userInfo

            val result = userRepository.getUserInfo(TEST_USER_ID).first()

            // Assert the success state
            assertTrue(result is Response.Success)
        }


    @Test
    fun `Given a network error in getUserInfo API call, When getUserInfo is invoked, Then it should show Error`() =
        runTest {
            val expectedException = IOException(NETWORK_ERROR_MESSAGE)

            coEvery { apiService.getUserInfo(TEST_USER_ID) } throws expectedException

            val result = userRepository.getUserInfo(TEST_USER_ID).first()

            assertTrue(result is Response.Error)
        }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private companion object {
        private const val TEST_USER_ID = "1"
        private const val NETWORK_ERROR_MESSAGE = "Network error"
    }
}


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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

class UserRepositoryImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var apiService: ApiService
    private lateinit var userMapper: UserMapper
    private lateinit var userInfoMapper: UserInfoMapper

    @Before
    fun setUp() {
        apiService = mockk()
        userMapper = mockk()
        userInfoMapper = mockk()
        userRepository =
            UserRepositoryImpl(apiService, userMapper = userMapper, userInfoMapper = userInfoMapper)
    }

    @Test
    fun `Given successful getUsers API call, When getUsers is invoked, Then it should return list of users`() =
        runTest {
            // Arrange
            val users = readUsersFromFile()
            val expectedUsers = users.map { User(it.id, it.name, it.email) }

            // Given
            coEvery { apiService.getUsers() } returns users
            every { userMapper.map(users) } returns expectedUsers

            // When
            val result = userRepository.getUsers().toList()

            // Then
            assertEquals(1, result.size)
            assertEquals(expectedUsers, (result[0] as Response.Success).data)
        }

    @Test
    fun `Given a network error in getUsers API call, When getUsers is invoked, Then it should handle the error`() =
        runTest {
            // Arrange
            val expectedException = IOException("Network error")

            // Given
            coEvery { apiService.getUsers() } throws expectedException

            // When
            val result = userRepository.getUsers().toList()

            // Then
            assert(result.size == 1)
            val responseError = result[0] as Response.Error
            assertEquals(expectedException, responseError.exception)
        }

    @Test
    fun `Given successful getUserInfo API call, When getUserInfo is invoked, Then it should return user info`() =
        runTest {
            // Arrange
            val userInfo = readUsersInfoFromFile()
            val expectedUserInfo = UserInfo(
                name = userInfo.name,
                email = userInfo.email,
                username = userInfo.username,
                address = userInfo.address?.let { addressDto ->
                    UserInfo.Address(
                        zipcode = addressDto.zipcode,
                        city = addressDto.city,
                        street = addressDto.street
                    )
                }
            )

            // Given
            coEvery { apiService.getUserInfo("1") } returns userInfo
            every { userInfoMapper.map(userInfo) } returns expectedUserInfo

            // When
            val flow = userRepository.getUserInfo("1")

            // Then
            flow.collect { response ->
                // Assert the initial loading state if needed

                // Assert the success state
                assertTrue(response is Response.Success)

                // Assert the value inside the success state
                val userInfo = (response as Response.Success).data
                assertEquals(expectedUserInfo, userInfo)
            }
        }

    @Test
    fun `Given a network error in getUserInfo API call, When getUserInfo is invoked, Then it should handle the error`() =
        runTest {
            // Arrange
            val testData = readUsersInfoFromFile()
            val expectedException = IOException("Network error")

            // Given
            coEvery { apiService.getUserInfo("1") } throws expectedException

            // When
            val result = userRepository.getUserInfo("1").toList()

            // Then
            assert(result.size == 1)
            val responseError = result[0] as Response.Error
            assertEquals(expectedException, responseError.exception)
        }


    private fun readUsersFromFile(): List<UserDto> {
        val jsonFile = File(javaClass.classLoader.getResource("UserTestData.json")!!.file)
        val jsonString = jsonFile.readText()
        val listType: Type = object : TypeToken<List<UserDto>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    private fun readUsersInfoFromFile(): UserInfoDto {
        val jsonFile = File(javaClass.classLoader.getResource("UserInfoTestData.json")!!.file)
        val jsonString = jsonFile.readText()
        val listType: Type = object : TypeToken<UserInfoDto>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}

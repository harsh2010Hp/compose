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

    private val testUserId = "1"
    private val networkErrorMessage = "Network error"
    private val userTestDataFileName = "UserTestData.json"
    private val userInfoTestDataFileName = "UserInfoTestData.json"

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
            val users = readUsersFromFile()
            val expectedUsers = users.map { User(it.id!!, it.name!!, it.email!!) }

            coEvery { apiService.getUsers() } returns users
            every { userMapper.map(users) } returns expectedUsers

            val result = userRepository.getUsers().toList()

            assertEquals(1, result.size)
            assertEquals(expectedUsers, (result[0] as Response.Success).data)
        }

    @Test
    fun `Given a network error in getUsers API call, When getUsers is invoked, Then it should handle the error`() =
        runTest {
            val expectedException = IOException(networkErrorMessage)

            coEvery { apiService.getUsers() } throws expectedException

            val result = userRepository.getUsers().toList()

            assert(result.size == 1)
            val responseError = result[0] as Response.Error
            assertEquals(expectedException, responseError.exception)
        }

    @Test
    fun `Given successful getUserInfo API call, When getUserInfo is invoked, Then it should return user info`() =
        runTest {
            val userInfo = readUsersInfoFromFile()
            val expectedUserInfo = UserInfo(
                name = userInfo.name!!,
                email = userInfo.email!!,
                username = userInfo.username!!,
                address = userInfo.address!!.run {
                    UserInfo.Address(
                        zipcode = zipcode!!,
                        city = city!!,
                        street = street!!
                    )
                }
            )

            coEvery { apiService.getUserInfo(testUserId) } returns userInfo
            every { userInfoMapper.map(userInfo) } returns expectedUserInfo

            val flow = userRepository.getUserInfo(testUserId)

            flow.collect { response ->
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
            val expectedException = IOException(networkErrorMessage)

            coEvery { apiService.getUserInfo(testUserId) } throws expectedException

            val result = userRepository.getUserInfo(testUserId).toList()

            assert(result.size == 1)
            val responseError = result[0] as Response.Error
            assertEquals(expectedException, responseError.exception)
        }


    private fun readUsersFromFile(): List<UserDto> {
        val jsonFile = File(javaClass.classLoader.getResource(userTestDataFileName)!!.file)
        val jsonString = jsonFile.readText()
        val listType: Type = object : TypeToken<List<UserDto>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    private fun readUsersInfoFromFile(): UserInfoDto {
        val jsonFile = File(javaClass.classLoader.getResource(userInfoTestDataFileName)!!.file)
        val jsonString = jsonFile.readText()
        val listType: Type = object : TypeToken<UserInfoDto>() {}.type
        return Gson().fromJson(jsonString, listType)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }
}

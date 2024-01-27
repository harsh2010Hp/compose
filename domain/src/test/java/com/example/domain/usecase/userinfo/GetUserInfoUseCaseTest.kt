package com.example.domain.usecase.userinfo

import com.example.core.Response
import com.example.domain.model.UserInfo
import com.example.domain.repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.lang.reflect.Type


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
            val users = readUsersInfoFromFile()
            coEvery { userRepository.getUserInfo("123") } returns flowOf(Response.Success(users))

            val getUserInfoUseCase = GetUserInfoUseCase(userRepository)

            // When
            val response = getUserInfoUseCase("123").toList()

            // Then
            assertEquals(Response.Success(users).data?.name, response.first().data?.name)
        }

    private fun readUsersInfoFromFile(): UserInfo {
        val jsonFile = File(javaClass.classLoader.getResource("UserInfoTestData.json")!!.file)
        val jsonString = jsonFile.readText()
        val listType: Type = object : TypeToken<UserInfo>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}
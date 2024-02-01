package com.example.domain.usecase.user

import com.example.core.Response
import com.example.domain.model.User
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
class GetUserUseCaseTest {

    @MockK
    private lateinit var userRepository: UserRepository

    private val fileName = "UserTestData.json"

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given users available, When getUserUseCase invoked, Then return success response`() =
        runTest {
            val users = readUsersFromFile()
            coEvery { userRepository.getUsers() } returns flowOf(Response.Success(users))

            val getUserUseCase = GetUserUseCase(userRepository)

            val response = getUserUseCase().toList()

            assertEquals(Response.Success(users).data?.first(), response.first().data?.first())
        }

    private fun readUsersFromFile(): List<User> {
        val jsonFile = File(javaClass.classLoader.getResource(fileName)!!.file)
        val jsonString = jsonFile.readText()
        val listType: Type = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}
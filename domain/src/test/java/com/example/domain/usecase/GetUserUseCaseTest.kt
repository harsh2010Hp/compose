package com.example.domain.usecase

import android.content.Context
import android.net.http.HttpException
import com.example.common.Response
import com.example.domain.R
import com.example.domain.model.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class GetUserUseCaseTest {

    private lateinit var getUserUseCase: GetUserUseCase
    private lateinit var testUserRepository: TestUserRepository
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = Mockito.mock(Context::class.java)
        Mockito.`when`(context.getString(R.string.default_error)).thenReturn("Error message")
        Mockito.`when`(context.getString(R.string.error_internet_connection))
            .thenReturn("Check your internet connection")
        testUserRepository = TestUserRepository()
        getUserUseCase = GetUserUseCase(testUserRepository, context)
    }

    @Test
    fun testGetUsersSuccess() = runBlocking {
        val result = getUserUseCase().first()
        assert(result is Response.Success)
        assertEquals(2, (result as Response.Success).data?.size)
    }

    @Test
    fun testGetUsersHttpException() = runBlocking {
        // Override the getUsers() function in the testUserRepository to throw an HttpException
        testUserRepository = object : TestUserRepository() {
            override suspend fun getUsers(): List<User> {
                throw HttpException("Error message", Throwable("Error message"))
            }
        }
        getUserUseCase = GetUserUseCase(testUserRepository, context)

        val result = getUserUseCase().first()
        assert(result is Response.Error)
        assertEquals("Error message", (result as Response.Error).message)
    }

    @Test
    fun testGetUsersIOException() = runBlocking {
        // Override the getUsers() function in the testUserRepository to throw an IOException
        testUserRepository = object : TestUserRepository() {
            override suspend fun getUsers(): List<User> {
                throw IOException()
            }
        }
        getUserUseCase = GetUserUseCase(testUserRepository, context)

        val result = getUserUseCase().first()
        assert(result is Response.Error)
        assertEquals(
            context.getString(R.string.error_internet_connection),
            (result as Response.Error).message
        )
    }
}
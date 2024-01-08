package com.example.lloyddemoapplication.presenter.home

import com.example.lloyddemoapplication.MainCoroutineRule
import com.example.lloyddemoapplication.data.model.User
import com.example.lloyddemoapplication.domain.usecase.GetUserUseCase
import com.example.lloyddemoapplication.utils.Response
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var homeViewModel: HomeViewModel

    @Mock
    private lateinit var getUserUseCase: GetUserUseCase


    @Before
    fun setUp() {
        getUserUseCase = Mockito.mock(GetUserUseCase::class.java)
        homeViewModel = HomeViewModel(getUserUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetUsersSuccess() = mainCoroutineRule.runBlockingTest{
        // Prepare mock data
        val users = listOf(
            User(name = "User 1", email = "user1@example.com"),
            User(name = "User 2", email = "user2@example.com")
        )

        // Configure GetUserUseCase mock to return mock data
        Mockito.`when`(getUserUseCase.invoke()).thenReturn(flowOf(Response.Success(users)))

        // Check if loading state is set to true before API call
        assertEquals(false, homeViewModel.loading.value)

        // Check if users are received correctly
        // Call getUsers() function in HomeViewModel
        val results = mutableListOf<Response<List<User>>>()
        val job = launch {
            homeViewModel.users.toList(results)
        }
        homeViewModel.getUsers()
        job.cancel()

        // Check if loading state is set to true before API call
        assertEquals(false, homeViewModel.loading.value)

        // Check if users are received correctly
        assertEquals(users.first(), results.last().data?.first())

        // Check if loading state is set to false after API call
        assertEquals(false, homeViewModel.loading.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetUsersError() = runTest {
        // Configure GetUserUseCase mock to return an error response
        Mockito.`when`(getUserUseCase.invoke())
            .thenReturn(flowOf(Response.Error("Error fetching users")))

        // Check if users are received correctly
        // Call getUsers() function in HomeViewModel
        val results = mutableListOf<Response<List<User>>>()
        val job = launch { homeViewModel.users.toList(results) }
        // Call getUsers() function in HomeViewModel
        homeViewModel.getUsers()

        // Check if loading state is set to true before API call
        assertEquals(false, homeViewModel.loading.value)
        job.cancel()

        // Check if an error response is received
        val errorResult = homeViewModel.users.first()
        assert(errorResult is Response.Error)
        assertEquals("Error fetching users", (errorResult as Response.Error).message)

        // Check if loading state is set to false after API call
        assertEquals(false, homeViewModel.loading.value)

        // Check if the error dialog is displayed
        assertEquals(true, homeViewModel.showDialog.value)
        assertEquals("Error fetching users", homeViewModel.errorMessage.value)
    }

    @Test
    fun testDismissErrorDialog() {
        // Call dismissErrorDialog() function in HomeViewModel
        homeViewModel.dismissErrorDialog()

        // Check if showDialog and errorMessage values are reset
        assertEquals(false, homeViewModel.showDialog.value)
        assertEquals(null, homeViewModel.errorMessage.value)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
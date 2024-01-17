package com.example.user_feature.userinfo

import androidx.lifecycle.SavedStateHandle
import com.example.core.Response
import com.example.domain.model.UserInfo
import com.example.domain.usecase.userinfo.GetUserInfoUseCase
import com.example.user_feature.presenter.userinfo.UserInfoViewModel
import com.example.user_feature.presenter.userinfo.intent.UserInfoIntent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UserInfoViewModelTest {

    @MockK
    private lateinit var getUserInfoUseCase: GetUserInfoUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var userInfoViewModel: UserInfoViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        savedStateHandle = SavedStateHandle(mapOf("userid" to "1"))
        userInfoViewModel = UserInfoViewModel(getUserInfoUseCase, savedStateHandle)
    }

    @Test
    fun `Given user ID, When fetching user info, Then success state is emitted`() =
        runTest {
            // Given
            val userId = "1"
            val userInfo = UserInfo("John", "Doe", "john.doe@example.com")
            coEvery { getUserInfoUseCase(userId) } returns flowOf(Response.Success(userInfo))

            // When
            userInfoViewModel.processIntent(UserInfoIntent.FetchUserInfo)

            // Then
            val viewState = userInfoViewModel.userInfoState.take(1).toList().first()
            assertEquals(userInfo, viewState.userInfo)
            assertFalse(viewState.isLoading)
            assertNull(viewState.errorMessage)
        }

    @Test
    fun `Given user ID, When fetching user info, Then error state is emitted`() = runTest {
        // Given
        val userId = "1"
        val errorMessage = "Error message"
        coEvery { getUserInfoUseCase(userId) } returns flowOf(Response.Error(Throwable(errorMessage)))

        // When
        userInfoViewModel.processIntent(UserInfoIntent.FetchUserInfo)
        // Then
        val job = launch {
            val viewState = userInfoViewModel.userInfoState.take(1).toList().first()
            assertNull(null, viewState.userInfo)
            assertFalse(viewState.isLoading)
            assertEquals(errorMessage, viewState.errorMessage)
        }
        job.cancel()
    }

    @Test
    fun `Given DialogDismissClicked intent, When dialog is dismissed, Then emit null error message`() =
        runTest {
            // Given
            val userId = "1"
            val errorMessage = "Error message"
            coEvery { getUserInfoUseCase(userId) } returns flowOf(
                Response.Error(
                    Exception(
                        errorMessage
                    )
                )
            )
            // When
            userInfoViewModel.processIntent(UserInfoIntent.UIIntent.DismissErrorDialog)
            // Then
            val viewState = userInfoViewModel.userInfoState.take(1).toList().first()
            assertNull(viewState.errorMessage)
        }
}
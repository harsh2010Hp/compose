package com.example.userfeature.userinfo

import androidx.lifecycle.SavedStateHandle
import com.example.core.Response
import com.example.domain.model.UserInfo
import com.example.domain.usecase.userinfo.GetUserInfoUseCase
import com.example.userfeature.presenter.userinfo.UserInfoViewModel
import com.example.userfeature.presenter.userinfo.effect.UserInfoEffect
import com.example.userfeature.presenter.userinfo.intent.UserInfoIntent
import com.example.userfeature.presenter.userinfo.state.UserInfoUIState
import com.example.userfeature.presenter.utils.Constants
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UserInfoViewModelTest {

    @MockK
    private lateinit var getUserInfoUseCase: GetUserInfoUseCase

    private lateinit var savedStateHandle: SavedStateHandle

    private lateinit var userInfoViewModel: UserInfoViewModel

    private val userId = "1"
    private val errorMessage = "Error message"
    private val errorValue = "1"
    private val fileName = "UserInfoTestData.json"


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        savedStateHandle = SavedStateHandle(mapOf(Constants.userId to errorValue))
        userInfoViewModel = UserInfoViewModel(getUserInfoUseCase, savedStateHandle)
    }

    @Test
    fun `Given user ID, When fetching user info, Then success state is emitted`() =
        runTest {
            val userInfo = readUsersInfoFromFile()
            coEvery { getUserInfoUseCase(userId) } returns flowOf(Response.Success(userInfo))

            userInfoViewModel.processIntent(UserInfoIntent.FetchUserInfo)

            val userInfoState = userInfoViewModel.userInfoState
            assert(userInfoState.value is UserInfoUIState.ShowContent)
            assert((userInfoState.value as UserInfoUIState.ShowContent).userInfo == userInfo)
        }

    @Test
    fun `Given user ID, When fetching user info, Then error state is emitted`() = runTest {
        coEvery { getUserInfoUseCase(userId) } returns flowOf(Response.Error(Throwable(errorMessage)))

        userInfoViewModel.processIntent(UserInfoIntent.FetchUserInfo)

        val job = launch {
            val userInfoState = userInfoViewModel.userInfoState
            assert(userInfoState.value is UserInfoUIState.Error)
            assert((userInfoState.value as UserInfoUIState.Error).showMessage)
        }
        job.cancel()
    }

    @Test
    fun `Given DialogDismissClicked intent, When dialog is dismissed, Then emit null error message`() =
        runTest {
            coEvery { getUserInfoUseCase(userId) } returns flowOf(
                Response.Error(
                    Exception(
                        errorMessage
                    )
                )
            )

            userInfoViewModel.processIntent(UserInfoIntent.UIIntent.DismissErrorDialog)

            val userInfoState = userInfoViewModel.userInfoState
            assert(userInfoState.value is UserInfoUIState.Error)
            assertNull((userInfoState.value as UserInfoUIState.Error).errorMessage)
        }

    @Test
    fun `Given UserInfoViewModel When back pressed Then navigate back`() =
        runTest {
            val userInfo = readUsersInfoFromFile()
            coEvery { getUserInfoUseCase(any()) } returns flow {
                emit(Response.Success(userInfo))
            }
            val userInfoViewModel = UserInfoViewModel(getUserInfoUseCase, savedStateHandle)

            userInfoViewModel.processIntent(UserInfoIntent.BackPressed.BackPressClicked)

            //  assert the result
            val job = launch {
                assert(userInfoViewModel.effect.first() is UserInfoEffect.BackPressEffect.BackPressed)
            }
            job.cancel()
        }

    private fun readUsersInfoFromFile(): UserInfo {
        val jsonFile = File(javaClass.classLoader?.getResource(fileName)!!.file)
        val jsonString = jsonFile.readText()
        return Gson().fromJson(jsonString, UserInfo::class.java)
    }
}
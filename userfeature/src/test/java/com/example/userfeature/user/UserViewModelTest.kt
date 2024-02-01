import com.example.core.Response
import com.example.domain.model.User
import com.example.domain.usecase.user.GetUserUseCase
import com.example.userfeature.presenter.user.UserViewModel
import com.example.userfeature.presenter.user.intent.UserIntent
import com.example.userfeature.presenter.user.state.UserUIState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import java.lang.reflect.Type

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UserViewModelTest {

    @MockK
    private lateinit var getUsersUseCase: GetUserUseCase
    private lateinit var userViewModel: UserViewModel

    private val errorMessage = "Error message"
    private val fileName = "UserTestData.json"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        userViewModel = UserViewModel(getUsersUseCase)
    }

    @Test
    fun `Given user list, When loading users, Then success state is emitted`() = runTest {
        val users = readUsersFromFile()
        coEvery { getUsersUseCase() } returns flowOf(Response.Success(users))

        userViewModel.processIntent(UserIntent.LoadUsers)
        val job = launch {
            val userState = userViewModel.userState
            assert(userState.value is UserUIState.ShowContent)
            assert((userViewModel.userState.value as UserUIState.ShowContent).users == users)
        }
        job.cancel()
    }

    @Test
    fun `Given user list, When loading users, Then error state is emitted`() = runTest {
        coEvery { getUsersUseCase() } returns flowOf(Response.Error(Exception(errorMessage)))

        userViewModel.processIntent(UserIntent.LoadUsers)

        val job = launch {
            val userState = userViewModel.userState
            assert(userState.value is UserUIState.Error)
            assert((userViewModel.userState.value as UserUIState.Error).showMessage)
        }
        job.cancel()
    }

    @Test
    fun `Given DialogDismissClicked intent, When dialog is dismissed, Then emit null error message`() =
        runTest {
            coEvery { getUsersUseCase() } returns flowOf(
                Response.Error(
                    Exception(
                        errorMessage
                    )
                )
            )

            userViewModel.processIntent(UserIntent.UIIntent.DialogDismissClicked)

            val userState = userViewModel.userState
            assert(userState.value is UserUIState.Error)
            assertNull((userViewModel.userState.value as UserUIState.Error).errorMessage)
        }

    private fun readUsersFromFile(): List<User> {
        val jsonFile = File(javaClass.classLoader?.getResource(fileName)!!.file)
        val jsonString = jsonFile.readText()
        val listType: Type = object : TypeToken<List<User>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}
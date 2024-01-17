import com.example.core.Response
import com.example.domain.model.User
import com.example.domain.usecase.user.GetUserUseCase
import com.example.user_feature.presenter.home.UserViewModel
import com.example.user_feature.presenter.home.intent.UserIntent
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
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
class UserViewModelTest {

    @MockK
    private lateinit var getUsersUseCase: GetUserUseCase
    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        userViewModel = UserViewModel(getUsersUseCase)
    }

    @Test
    fun `Given user list, When loading users, Then success state is emitted`() =
        runTest {
            // Given
            val users = listOf(
                User("1", "John", "Doe"),
                User("2", "Jane", "Doe")
            )
            coEvery { getUsersUseCase() } returns flowOf(Response.Success(users))
            // When
            userViewModel.processIntent(UserIntent.LoadUsers)
            val job = launch {
                // Then
                val userState = userViewModel.userState.take(2).toList().last()
                assertEquals(users, userState.users)
                assertEquals(false, userState.isLoading)
                assertEquals(null, userState.errorMessage)
            }
            job.cancel()
        }

    @Test
    fun `Given user list, When loading users, Then error state is emitted`() =
        runTest {
            // Given
            val errorMessage = "Error message"
            coEvery { getUsersUseCase() } returns flowOf(Response.Error(Exception(errorMessage)))
            // When
            userViewModel.processIntent(UserIntent.LoadUsers)
            // Then
            val job = launch {
                val userState = userViewModel.userState.take(2).toList().last()
                assertEquals(emptyList<User>(), userState.users)
                assertFalse(userState.isLoading)
                assertEquals(errorMessage, userState.errorMessage)
            }
            job.cancel()
        }

    @Test
    fun `Given DialogDismissClicked intent, When dialog is dismissed, Then emit null error message`() =
        runTest {
            // Given
            val errorMessage = "Error message"
            coEvery { getUsersUseCase() } returns flowOf(
                Response.Error(
                    Exception(
                        errorMessage
                    )
                )
            )
            // When
            userViewModel.processIntent(UserIntent.UIIntent.DialogDismissClicked)
            // Then
            val viewState = userViewModel.userState.take(1).toList().first()
            TestCase.assertNull(viewState.errorMessage)
        }
}
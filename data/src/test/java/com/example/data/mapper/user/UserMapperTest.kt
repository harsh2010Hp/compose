import com.example.data.mapper.user.UserMapper
import com.example.domain.model.User
import com.example.network.dto.UserDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File
import java.lang.reflect.Type

@RunWith(Parameterized::class)
class UserMapperTest(
    private val userDto: List<UserDto>,
    private val expectedUserId: String,
    private val expectedUserName: String,
    private val expectedUserEmail: String
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            val fileName = "UserTestData.json"
            val jsonFile =
                File(UserMapperTest::class.java.classLoader?.getResource(fileName)!!.file)
            val jsonString = jsonFile.readText()
            val listType: Type = object : TypeToken<List<UserDto>>() {}.type
            val userDtos: List<UserDto> = Gson().fromJson(jsonString, listType)

            return userDtos.map {
                arrayOf(
                    listOf(it),
                    it.id!!,
                    it.name!!,
                    it.email!!
                )
            }
        }
    }

    private lateinit var userMapper: UserMapper

    @Before
    fun setUp() {
        userMapper = mockk()
        every { userMapper.map(userDto) } returns userDto.map {
            User(
                it.id!!,
                it.name!!,
                it.email!!
            )
        }
    }

    @Test
    fun `Given a list of UserDto, When map is called, Then it should convert to a list of User`() {
        val userList = userMapper.map(userDto)

        assertEquals(1, userList.size)

        // Verify that the mapping is correct for each user
        assertEquals(expectedUserId, userList[0].id)
        assertEquals(expectedUserName, userList[0].name)
        assertEquals(expectedUserEmail, userList[0].email)
    }
}

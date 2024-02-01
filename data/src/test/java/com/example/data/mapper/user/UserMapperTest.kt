package com.example.data.mapper.user

import com.example.domain.model.User
import com.example.network.dto.UserDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.lang.reflect.Type

class UserMapperTest {

    private val fileName = "UserTestData.json"
    private val expectedUserId = "1"
    private val expectedUserName = "John"
    private val expectedUserEmail = "john@example.com"

    @Test
    fun `Given a list of UserDto, When map is called, Then it should convert to a list of User`() {
        val userDto = readUsersFromFile()
        val expectedUsers = userDto.map { User(it.id!!, it.name!!, it.email!!) }

        // Mocking the UserMapper
        val userMapper = mockk<UserMapper>()

        every { userMapper.map(userDto) } returns expectedUsers

        val userList = userMapper.map(userDto)

        assertEquals(1, userList.size)

        // Verify that the mapping is correct for each user
        assertEquals(expectedUserId, userList[0].id)
        assertEquals(expectedUserName, userList[0].name)
        assertEquals(expectedUserEmail, userList[0].email)
    }

    private fun readUsersFromFile(): List<UserDto> {
        val jsonFile = File(javaClass.classLoader?.getResource(fileName)!!.file)
        val jsonString = jsonFile.readText()
        val listType: Type = object : TypeToken<List<UserDto>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}

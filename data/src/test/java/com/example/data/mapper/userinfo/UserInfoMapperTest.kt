package com.example.data.mapper.userinfo

import com.example.domain.model.UserInfo
import com.example.network.dto.UserInfoDto
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class UserInfoMapperTest {

    private val testDataFileName = "UserInfoTestData.json"

    private val expectedName = "Leanne Graham"
    private val expectedEmail = "Sincere@april.biz"
    private val expectedUsername = "Bret"
    private val expectedZipcode = "92998-3874"
    private val expectedCity = "Gwenborough"
    private val expectedStreet = "Kulas Light"

    @Test
    fun `Given a UserInfoDto, When map is called, Then it should convert to UserInfo`() {
        val userInfoDto = readUsersInfoFromFile()
        val expectedUserInfo = UserInfo(
            name = expectedName,
            email = expectedEmail,
            username = expectedUsername,
            address = UserInfo.Address(
                zipcode = expectedZipcode,
                city = expectedCity,
                street = expectedStreet
            )
        )

        // Mocking the UserInfoMapper
        val userInfoMapper = mockk<UserInfoMapper>()

        every { userInfoMapper.map(userInfoDto) } returns expectedUserInfo

        val resultUserInfo = userInfoMapper.map(userInfoDto)

        assertEquals(expectedName, resultUserInfo.name)
        assertEquals(expectedEmail, resultUserInfo.email)
        assertEquals(expectedUsername, resultUserInfo.username)

        val address = resultUserInfo.address
        assertEquals(expectedZipcode, address.zipcode)
        assertEquals(expectedCity, address.city)
        assertEquals(expectedStreet, address.street)
    }

    private fun readUsersInfoFromFile(): UserInfoDto {
        val jsonFile = File(javaClass.classLoader?.getResource(testDataFileName)!!.file)
        val jsonString = jsonFile.readText()
        return Gson().fromJson(jsonString, UserInfoDto::class.java)
    }
}

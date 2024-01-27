package com.example.data.mapper.user

import com.example.core.mapper.Mapper
import com.example.domain.model.User
import com.example.network.dto.UserDto

class UserMapper : Mapper<List<UserDto?>, List<User>> {
    override fun map(usersDto: List<UserDto?>): List<User> {
        return usersDto.map {
            User(
                it?.id, it?.name, it?.email
            )
        }
    }
}

package com.example.data.mapper

import com.example.domain.model.User
import com.example.domain.model.UserInfo
import com.example.network.dto.UserInfoDto
import com.example.network.dto.UserDto

internal fun List<UserDto>.toUser(): List<User> {
    return this.map {
        User(
            it.id,it.name, it.email
        )
    }
}

internal fun UserInfoDto.toUserInfo(): UserInfo {
    return UserInfo(
        name,email, username, UserInfo.Address(address?.zipcode, address?.city, address?.street)
    )
}
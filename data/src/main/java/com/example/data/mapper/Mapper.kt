package com.example.data.mapper

import com.example.domain.model.Address
import com.example.domain.model.User
import com.example.network.model.UserResponse

internal fun List<UserResponse>.toUser(): List<User> {
    return this.map {
        User(
            it.name, it.email, it.username, it.company?.name,
            it.address?.let { address ->
                Address(address.zipcode, address.city, address.street)
            },
        )
    }
}
package com.example.data.mapper.userinfo

import com.example.core.mapper.Mapper
import com.example.domain.model.UserInfo
import com.example.network.dto.UserInfoDto

class UserInfoMapper : Mapper<UserInfoDto?, UserInfo> {
    override fun map(userInfoDto: UserInfoDto?): UserInfo {
        return with(userInfoDto) {
            UserInfo(
                name = this?.name,
                email = this?.email,
                username = this?.username,
                address = this?.address?.let { addressDto ->
                    UserInfo.Address(
                        zipcode = addressDto.zipcode,
                        city = addressDto.city,
                        street = addressDto.street
                    )
                }
            )
        }
    }
}

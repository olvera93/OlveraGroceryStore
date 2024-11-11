package com.olvera.groceries.util

import com.olvera.groceries.dto.UserInfoResponse
import com.olvera.groceries.model.AppUser
import org.springframework.stereotype.Component

@Component
class UserInfoMapper {

    fun toDto(entity: AppUser) = UserInfoResponse(
        firstName = entity.firstName,
        lastName = entity.lastName,
        email = entity.email,
        username = entity.username
    )

}
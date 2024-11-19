package com.olvera.groceries.util

import com.olvera.groceries.dto.RegisterRequest
import com.olvera.groceries.model.AppUser
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class SignUpMapper {

    fun toEntity(request: RegisterRequest, passwordEncoder: PasswordEncoder) = AppUser().apply {
        firstName = request.firstName
        lastName = request.lastName
        email = request.email
        appUsername = request.username
        clientPassword = passwordEncoder.encode(request.password)
    }

}
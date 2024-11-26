package com.olvera.groceries.util

import com.olvera.groceries.dto.RegisterRequest
import com.olvera.groceries.model.AppUser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class SignUpMapperTest {

    @Test
    fun `when register request to entity is mapped then check the properties`() {

        val mockPasswordEncoder = mockk<PasswordEncoder>()
        val request = RegisterRequest(
            firstName = "Gonzalo",
            lastName = "Olvera",
            email = "olvera@gmail.com",
            username = "Olvera",
            password = "test",
            passwordConfirmation = "test"
        )

        val encodedPassword = "encoderPassword"
        every { mockPasswordEncoder.encode(request.password) } returns encodedPassword
        val mapper = SignUpMapper()

        val user: AppUser = mapper.toEntity(request, mockPasswordEncoder)

        assertEquals(request.firstName, user.firstName)
        assertEquals(request.lastName, user.lastName)
        assertEquals(request.email, user.email)
        assertEquals(request.username, user.username)
        assertEquals(encodedPassword, user.password)


    }

}
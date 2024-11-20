package com.olvera.groceries.service

import com.olvera.groceries.dto.AuthenticationRequest
import com.olvera.groceries.dto.EmailConfirmedResponse
import com.olvera.groceries.dto.RegisterRequest
import com.olvera.groceries.error.SignUpException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.model.VerificationToken
import com.olvera.groceries.repository.AppUserRepository
import com.olvera.groceries.repository.VerificationTokenRepository
import com.olvera.groceries.service.impl.AccountManagementServiceImpl
import com.olvera.groceries.util.SignUpMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.time.temporal.ChronoUnit

class AccountManagementServiceImplTest {

    private val mockPasswordEncoder = mockk<PasswordEncoder>()
    private val mockJwtService = mockk<JwtService>()
    private val mockAuthenticationManager = mockk<AuthenticationManager>()
    private val mockMapper = mockk<SignUpMapper>()
    private val mockUserRepository = mockk<AppUserRepository>()
    private val mockTokenRepository = mockk<VerificationTokenRepository>()
    private val mockEmailService = mockk<EmailService>()

    private val registerRequest = RegisterRequest(
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@mail.com",
        username = "JohnDoe",
        password = "test",
        passwordConfirmation = "test"
    )

    private val user = AppUser(
        firstName = registerRequest.firstName,
        lastName = registerRequest.lastName,
        email = registerRequest.email,
        appUsername = registerRequest.username,
        clientPassword = registerRequest.password
    )

    private val verificationToken = VerificationToken(
        token = "a1.b2.c3",
        appUser = user,
        expiryDate = Instant.now().plus(1, ChronoUnit.DAYS)
    )

    private val authenticationRequest = AuthenticationRequest(
        "najw-kara", "hello123"
    )

    private val objectUnderTest = AccountManagementServiceImpl(
        mockPasswordEncoder,
        mockJwtService,
        mockAuthenticationManager,
        mockMapper,
        mockUserRepository,
        mockTokenRepository,
        mockEmailService
    )

    @Test
    fun `when user sign up is triggered then expect email get sent message`() {
        every { mockUserRepository.findByEmail(any()) } returns null
        every { mockUserRepository.findByAppUsername(any()) } returns null
        every { mockMapper.toEntity(any(), any()) } returns user
        every { mockUserRepository.save(any()) } returns user
        every { mockTokenRepository.save(any()) } returns verificationToken
        every { mockEmailService.sendVerificationEmail(any(), any()) } returns Unit

        val actualResult: EmailConfirmedResponse = objectUnderTest.signUp(registerRequest)

        assertEquals(
            "Please, check your email and spam/junk folder for ${user.email} to verify your account.",
            actualResult.message
        )
        verify { mockUserRepository.save(user) }
        verify { mockTokenRepository.save(any()) }

    }

    @Test
    fun `when user sign up is triggered then expect sign up exception for the email `() {
        every { mockUserRepository.findByEmail(any()) } returns user

        val actualResult = assertThrows<SignUpException> { objectUnderTest.signUp(registerRequest) }

        assertEquals("User email already exists!", actualResult.message)
        verify(exactly = 0) { mockUserRepository.save(user) }
        verify(exactly = 0) { mockTokenRepository.save(any()) }
    }

    @Test
    fun `when user sign up is triggered then expect sign up exception for the username `() {
        every { mockUserRepository.findByEmail(any()) } returns null
        every { mockUserRepository.findByAppUsername(any()) } returns user

        val actualResult = assertThrows<SignUpException> { objectUnderTest.signUp(registerRequest) }

        assertEquals("Username already exists!", actualResult.message)
        verify(exactly = 0) { mockUserRepository.save(user) }
        verify(exactly = 0) { mockTokenRepository.save(any()) }
    }

    @Test
    fun `when user sign up is triggered then expect sign up exception for the password `() {
        every { mockUserRepository.findByEmail(any()) } returns null
        every { mockUserRepository.findByAppUsername(any()) } returns null

        val request = RegisterRequest(
            firstName = "Paco",
            lastName = "Juarez",
            email = "paco.j@mail.com",
            username = "Paquito",
            password = "hello",
            passwordConfirmation = "bye"
        )

        val actualResult = assertThrows<SignUpException> { objectUnderTest.signUp(request) }

        assertEquals("Password and password confirmation does not match!", actualResult.message)
        verify(exactly = 0) { mockUserRepository.save(user) }
        verify(exactly = 0) { mockTokenRepository.save(any()) }
    }
}
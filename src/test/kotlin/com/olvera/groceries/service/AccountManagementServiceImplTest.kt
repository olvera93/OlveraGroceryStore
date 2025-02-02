package com.olvera.groceries.service

import com.olvera.groceries.dto.AuthenticationRequest
import com.olvera.groceries.dto.EmailConfirmedResponse
import com.olvera.groceries.dto.RegisterRequest
import com.olvera.groceries.error.*
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.model.VerificationToken
import com.olvera.groceries.repository.AppUserRepository
import com.olvera.groceries.repository.VerificationTokenRepository
import com.olvera.groceries.service.impl.AccountManagementServiceImpl
import com.olvera.groceries.util.SignUpMapper
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UsernameNotFoundException
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

    private val token = "valid-token1"


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

    @Test
    fun `when verify user is triggered then expect verified successfully message`() {

        every { mockTokenRepository.findByToken(token) } returns verificationToken
        every { mockUserRepository.save(user) } returns user

        val actualResult: EmailConfirmedResponse = objectUnderTest.verify(token)

        assertEquals("Account successfully verified.", actualResult.message)
        assertTrue(user.isVerified)
        verify { mockUserRepository.save(user) }
    }

    @Test
    fun `when verify user is triggered then expect invalid token message`() {

        every { mockTokenRepository.findByToken(token) } returns null

        val actualResult = assertThrows<AccountVerificationException> { objectUnderTest.verify(token) }

        assertEquals("Invalid Token!", actualResult.message)
        verify { mockTokenRepository.findByToken(token) }
    }

    @Test
    fun `when verify user is triggered then expect account is already verified message`() {

        val currentUser = AppUser()
        currentUser.isVerified = true

        val currentToken = VerificationToken(
            token = "a1.b2.c3",
            appUser = currentUser,
            expiryDate = Instant.now().plus(1, ChronoUnit.DAYS)
        )

        every { mockTokenRepository.findByToken(token) } returns currentToken

        val actualResult = assertThrows<AccountVerificationException> { objectUnderTest.verify(token) }

        assertEquals("Account is already verified for: $currentUser", actualResult.message)
        verify { mockTokenRepository.findByToken(token) }
        verify(exactly = 0) { mockUserRepository.save(currentUser) }
    }

    @Test
    fun `when verify user is triggered then expect token expired message`() {

        val expiredToken = VerificationToken(
            token = "a1.b2.c3",
            appUser = user,
            expiryDate = Instant.now().minusSeconds(3600)
        )

        every { mockTokenRepository.findByToken(token) } returns expiredToken
        every { mockTokenRepository.save(expiredToken) } returns expiredToken
        every { mockEmailService.sendVerificationEmail(user, any()) } returns Unit

        val actualResult = assertThrows<TokenExpiredException> { objectUnderTest.verify(token) }

        assertEquals(
            "Token expired, a new verification link has been sent to your email: ${user.email}",
            actualResult.message
        )
        verify { mockTokenRepository.findByToken(token) }
        verify { mockTokenRepository.save(expiredToken) }
        verify { mockEmailService.sendVerificationEmail(user, any()) }
    }

    @Test
    fun `when sign in user is called then expect authentication response`() {
        val mockAuthentication: Authentication = mockk(relaxed = true)
        val expectedAccessToken = "a1b2c3"
        val expectedRefreshToken = "d4e5f6"

        every { mockUserRepository.findByAppUsername(any()) } returns user
        user.isVerified = true

        every { mockAuthenticationManager.authenticate(any()) } returns mockAuthentication
        every { mockJwtService.generateAccessToken(user) } returns expectedAccessToken
        every { mockJwtService.generateRefreshToken(user) } returns expectedRefreshToken

        val actualResult = objectUnderTest.signIn(authenticationRequest)

        assertEquals(expectedAccessToken, actualResult.accessToken)
        assertEquals(expectedRefreshToken, actualResult.refreshToken)
    }

    @Test
    fun `when sign in user is called then expect user not found message`() {
        every { mockUserRepository.findByAppUsername(any()) } returns null

        val actualResult: UsernameNotFoundException = assertThrows { objectUnderTest.signIn(authenticationRequest) }

        assertEquals("User not found!", actualResult.message)
    }

    @Test
    fun `when sign in user is called then expect verification link not clicked message`() {

        every { mockUserRepository.findByAppUsername(any()) } returns user
        user.isVerified = false

        val actualResult: AccountVerificationException = assertThrows { objectUnderTest.signIn(authenticationRequest) }

        assertEquals(
            "You did not click on the verification link yet, check your mails for ${user.email}",
            actualResult.message
        )

        verify { mockUserRepository.findByAppUsername(any()) }
        verify(exactly = 0) { mockAuthenticationManager.authenticate(any()) }
    }

    @Test
    fun `when sign in user is called then expect username or password is incorrect message`() {
        user.isVerified = true

        val bce = BadCredentialsException("Username or password is incorrect")

        every { mockUserRepository.findByAppUsername(any()) } returns user
        every { mockAuthenticationManager.authenticate(any()) } throws bce

        val actualResult: UsernamePasswordMismatchException =
            assertThrows { objectUnderTest.signIn(authenticationRequest) }

        assertEquals("Username or password is incorrect", actualResult.message)
        verify { mockUserRepository.findByAppUsername(any()) }
        verify { mockAuthenticationManager.authenticate(any()) }
    }

    @Test
    fun `when reset password for user is called then expect now password sent message`() {

        val password = "top-secret-password"
        every { mockUserRepository.findByEmail(any()) } returns user
        every { mockPasswordEncoder.encode(any()) } returns password
        every { mockUserRepository.save(user) } returns user
        every { mockEmailService.sendPasswordResetEmail(any(), any()) } returns Unit

        val actualResult = objectUnderTest.resetPassword(user.email)

        assertEquals("New password sent to ${user.email} successfully.", actualResult.message)

    }

    @Test
    fun `when reset password for user is called then expect email does not exist message`() {

        every { mockUserRepository.findByEmail(any()) } returns null

        val actualResult: UserNotFoundException = assertThrows { objectUnderTest.resetPassword(user.email) }

        assertEquals("Email: ${user.email} does not exist!", actualResult.message)

        verify { mockUserRepository.findByEmail(any()) }
        verify(exactly = 0) { mockPasswordEncoder.encode(any()) }
        verify(exactly = 0) { mockUserRepository.save(any()) }
        verify(exactly = 0) { mockEmailService.sendVerificationEmail(any(), any()) }


    }


}
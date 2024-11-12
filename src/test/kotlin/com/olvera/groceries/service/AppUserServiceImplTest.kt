package com.olvera.groceries.service

import com.olvera.groceries.dto.UserInfoResponse
import com.olvera.groceries.dto.UserInfoUpdateRequest
import com.olvera.groceries.dto.UserPasswordUpdateRequest
import com.olvera.groceries.error.BadRequestException
import com.olvera.groceries.error.PasswordMismatchException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.repository.AppUserRepository
import com.olvera.groceries.service.impl.AppUserServiceImpl
import com.olvera.groceries.util.UserInfoMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.crypto.password.PasswordEncoder

class AppUserServiceImplTest {

    private val mockPasswordEncoder = mockk<PasswordEncoder>(relaxed = true)

    private val mockRepository = mockk<AppUserRepository>(relaxed = true)

    private val mockMapper = mockk<UserInfoMapper>(relaxed = true)

    private val mockSessionService = mockk<ClientSessionService>(relaxed = true)

    private val mockInfoResponse = mockk<UserInfoResponse>()

    private val updateRequest = UserInfoUpdateRequest(firstName = "Paco", lastName = "Perez")

    private val user = AppUser(
        firstName = "Gonzalo",
        lastName = "Olvera",
        email = "olvera@gmail.com",
        appUsername = "test",
        clientPassword = "SecurePassword1234!",
        isVerified = true,
    )

    private val requestMap = HashMap<String, String>()

    private val objectUnderTest =
        AppUserServiceImpl(mockPasswordEncoder, mockRepository, mockMapper, mockSessionService)

    @Test
    fun `when change user email is called then check for the response`() {
        requestMap["email"] = "hello@test.com"
        every { mockSessionService.findCurrentSessionUser() } returns user
        every { mockRepository.findByEmail(any()) } returns user
        every { mockRepository.save(any()) } returns user
        every { mockMapper.toDto(user) } returns mockInfoResponse

        val actualResult = objectUnderTest.changeEmail(requestMap)

        assertNotNull(actualResult)
        assertEquals(mockInfoResponse, actualResult)
        verify { mockRepository.save(any()) }
        verify { mockMapper.toDto(any()) }
    }

    @Test
    fun `when change user email is called then expect bad request exception for invalid email format`() {
        requestMap["email"] = "test.com"

        every { mockSessionService.findCurrentSessionUser() } returns user


        val actualException = assertThrows<BadRequestException> { objectUnderTest.changeEmail(requestMap) }

        assertEquals("Invalid email format", actualException.message)
        verify(exactly = 0) { mockRepository.save(any()) }
    }

    @Test
    fun `when change user email is called then expect bad request exception for missing email`() {

        val map = HashMap<String, String?>()

        map["email"] = null

        every { mockSessionService.findCurrentSessionUser() } returns user


        val actualException = assertThrows<BadRequestException> { objectUnderTest.changeEmail(requestMap) }

        assertEquals("The new email is missing in the request", actualException.message)
        verify(exactly = 0) { mockRepository.save(any()) }
    }

    @Test
    fun `when change user email is called then expect bad request exception for email already in use`() {

        val email = "olvera@gmail.com"

        requestMap["email"] = email
        val existingUser = AppUser(
            id = 2L,
            firstName = "Gonzalo",
            lastName = "Olvera",
            email = email,
            appUsername = "test",
            clientPassword = "SecurePassword1234!",
            isVerified = true,
        )

        every { mockSessionService.findCurrentSessionUser() } returns user
        every { mockRepository.findByEmail(email) } returns existingUser

        val actualException = assertThrows<BadRequestException> { objectUnderTest.changeEmail(requestMap) }

        assertEquals("Email is already used by another user", actualException.message)
        verify(exactly = 0) { mockRepository.save(any()) }
        verify { mockRepository.findByEmail(email) }
    }

    @Test
    fun `when change user password is called then expect password changed successfully`() {

        val expectedPassword = "new-password"
        val request = UserPasswordUpdateRequest("old-password", expectedPassword, expectedPassword)

        every { mockSessionService.findCurrentSessionUser() } returns user
        every { mockPasswordEncoder.matches(any(), any()) } returns true
        every { mockRepository.save(any()) } returns user

        objectUnderTest.changePassword(request)

        assertEquals(expectedPassword, request.newPassword)

        verify { mockPasswordEncoder.encode(any()) }
        verify { mockRepository.save(any()) }

    }

    @Test
    fun `when change user password gets called then expect current password wrong exception`() {
        val expectedPassword = "my-new-password"
        val request = UserPasswordUpdateRequest("current-password", expectedPassword, expectedPassword)

        user.clientPassword = "MyTestPassword123456789"

        val actualException = assertThrows<PasswordMismatchException> { objectUnderTest.changePassword(request) }

        assertEquals("The current password is wrong!", actualException.message)

        verify(exactly = 0) { mockPasswordEncoder.encode(any()) }
        verify(exactly = 0) { mockRepository.save(any()) }
    }

    @Test
    fun `when change user password gets called then expect password confirmation does not match exception`() {

        val expectedPassword = "my-new-password"
        val request = UserPasswordUpdateRequest("test", expectedPassword, "myNewPassword")

        every { mockPasswordEncoder.matches(any(), any()) } returns true

        val actualException = assertThrows<PasswordMismatchException> { objectUnderTest.changePassword(request) }


        assertEquals("Your new password does not match with the password confirmation!", actualException.message)

        verify(exactly = 0) { mockPasswordEncoder.encode(any()) }
        verify(exactly = 0) { mockRepository.save(any()) }

    }

    @Test
    fun `when change user info is called then expect success response`() {

        every { mockSessionService.findCurrentSessionUser() } returns user
        every { mockRepository.save(any()) } returns user
        every { mockMapper.toDto(user) } returns mockInfoResponse

        val actualResponse: UserInfoResponse = objectUnderTest.changeInfo(updateRequest)

        assertEquals(mockInfoResponse, actualResponse)

        verify { mockRepository.save(any()) }
        verify { mockMapper.toDto(any()) }
    }

     @Test
     fun `when fetch user is called then expect success response`() {

         every { mockSessionService.findCurrentSessionUser() } returns user
         every { mockMapper.toDto(user) } returns mockInfoResponse

         val actualResponse: UserInfoResponse = objectUnderTest.fetchInfo()

         assertEquals(mockInfoResponse, actualResponse)

         verify { mockMapper.toDto(any()) }
     }

}
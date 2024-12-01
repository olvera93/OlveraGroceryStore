package com.olvera.groceries.service

import com.olvera.groceries.error.UserNotFoundException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.repository.AppUserRepository
import com.olvera.groceries.service.impl.ClientSessionServiceImpl
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

@ExtendWith(MockKExtension::class)
class ClientSessionServiceImplTest {

    @RelaxedMockK
    private lateinit var mockSecurityContext: SecurityContext

    @RelaxedMockK
    private lateinit var mockAuthentication: Authentication

    @RelaxedMockK
    private lateinit var mockUserRepository: AppUserRepository

    private val user = AppUser()

    private lateinit var objectUnderTest: ClientSessionService

    @BeforeEach
    fun setUp() {
        every { mockSecurityContext.authentication } returns mockAuthentication
        SecurityContextHolder.setContext(mockSecurityContext)
        objectUnderTest = ClientSessionServiceImpl(mockUserRepository)
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }


    @Test
    fun `when retrieve authentication is called then expect matching objects`() {
        val actualResult: Authentication? = objectUnderTest.retrieveAuthentication()

        assertEquals(mockAuthentication, actualResult)
    }

    @Test
    fun `when get authenticated user is called then expect the response to match global user field`(){

        every { mockAuthentication.principal } returns user

        val actualResult: AppUser = objectUnderTest.getAuthenticatedUser()

        assertEquals(user, actualResult)
    }

    @Test
    fun `when find current session user is called then expect the response to match global user field`() {
        every { mockUserRepository.findByAppUsername(any()) } returns user

        val actualResult: AppUser = objectUnderTest.findCurrentSessionUser()

        assertEquals(user, actualResult)
    }

    @Test
    fun `when find current session user is called then expect authenticated user not found message`() {

        every { mockSecurityContext.authentication } returns null

        val actualResult: UserNotFoundException = assertThrows { objectUnderTest.findCurrentSessionUser() }

        assertEquals("Authenticated user not found", actualResult.message)
    }

    @Test
    fun `when find current session user is called then expect user not found with username message`() {
        every { mockAuthentication.principal } returns user
        every { mockUserRepository.findByAppUsername(any()) } returns null

        val actualResult: UserNotFoundException = assertThrows { objectUnderTest.findCurrentSessionUser() }

        assertEquals("User ${user.username} not found", actualResult.message)
    }


}
package com.olvera.groceries.util

import com.olvera.groceries.dto.UserInfoResponse
import com.olvera.groceries.model.AppUser
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserInfoMapperTest {

    private val objectUnderTest = UserInfoMapper()

    @Test
    fun `when entity to dto is called then check for the response properties`() {

        val user =  mockk<AppUser>()
        every { user.firstName } returns "Maya"
        every { user.lastName } returns "Biab"
        every { user.email } returns "maya.biab@mtv.lab"
        every { user.username } returns "maya-biab"

        val actualResponse: UserInfoResponse = objectUnderTest.toDto(user)

        assertEquals(user.firstName, actualResponse.firstName)
        assertEquals(user.lastName, actualResponse.lastName)
        assertEquals(user.email, actualResponse.email)
        assertEquals(user.username, actualResponse.username)
    }

}
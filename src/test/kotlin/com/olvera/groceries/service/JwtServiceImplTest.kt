package com.olvera.groceries.service

import com.olvera.groceries.error.JwtAuthenticationException
import com.olvera.groceries.service.impl.JwtServiceImpl
import com.olvera.groceries.util.JwtKey
import io.jsonwebtoken.Jwts
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.core.userdetails.UserDetails
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.crypto.SecretKey

@ExtendWith(MockKExtension::class)
class JwtServiceImplTest {

    private val mockJwtKey: JwtKey = mockk(relaxed = true)

    private val username = "abu-ali"

    private lateinit var secretKey: SecretKey

    private val objectUnderTest = JwtServiceImpl(mockJwtKey)

    companion object {
        private val EXPIRATION_ONE_DAY = TimeUnit.DAYS.toMillis(1)
        private val EXPIRATION_SEVEN_DAYS = TimeUnit.DAYS.toMillis(7)
    }

    @BeforeEach
    fun setUp() {
        secretKey = Jwts.SIG.HS256.key().build()
        every { mockJwtKey.secretKey } returns secretKey
    }

    @Test
    fun `when extract username is called then expect username matches with result`() {
        val token = Jwts.builder().claim("sub", username).signWith(secretKey).compact()

        val actualResult: String = objectUnderTest.extractUsername(token)

        assertEquals(username, actualResult)
    }

    @Test
    fun `when extract username is called then expect failed to extract claims from token message`() {

        val actualResult: JwtAuthenticationException =
            assertThrows { objectUnderTest.extractUsername("not-signed-token") }

        assertEquals("Failed to extract claims from token", actualResult.message)
    }

    @Test
    fun `when is token valid is called then expect result equals true`() {

        val expirationInOneHour = Date(System.currentTimeMillis() + 3600 * 1000)
        val token = Jwts.builder()
            .claim("sub", username)
            .claim("exp", expirationInOneHour)
            .signWith(secretKey)
            .compact()


        val mockUserDetails = mockk<UserDetails>()
        every { mockUserDetails.username } returns username

        val actualResult: Boolean = objectUnderTest.isTokenValid(token, mockUserDetails)

        assertTrue(actualResult)

    }


}
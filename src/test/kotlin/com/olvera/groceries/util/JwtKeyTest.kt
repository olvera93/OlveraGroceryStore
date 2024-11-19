package com.olvera.groceries.util

import com.olvera.groceries.error.JwtKeyException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import javax.crypto.SecretKey

class JwtKeyTest {

    private val mockSecretKey = mockk<SecretKey>(relaxed = true)
    private val mockKeyGenerator = mockk<KeyGenerator>()
    private lateinit var objectUnderTest: JwtKey

    @BeforeEach
    fun setUp() {
        every { mockKeyGenerator.generateHmacShaKey() } returns mockSecretKey
        objectUnderTest = JwtKey(mockKeyGenerator)

    }

    @Test
    fun `when secret key is generated then check for not null`() {
        val key: SecretKey = assertDoesNotThrow { objectUnderTest.secretKey }

        assertNotNull(key)
    }

    @Test
    fun `when secret key is generated then check for log error and exception`() {
        every { mockKeyGenerator.generateHmacShaKey() } throws IllegalStateException()

        val actualException: JwtKeyException = assertThrows<JwtKeyException> { objectUnderTest.secretKey }

        assertEquals("Invalid JWT secret key", actualException.message)
    }
    
}
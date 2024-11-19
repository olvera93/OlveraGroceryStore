package com.olvera.groceries.repository

import com.olvera.groceries.model.AppUser
import com.olvera.groceries.model.VerificationToken
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.Instant
import java.time.temporal.ChronoUnit

@DataJpaTest
class VerificationTokenRepositoryIT @Autowired constructor(
    val entityManager: TestEntityManager,
    val objectUnderTest: VerificationTokenRepository
) {

    private val user = AppUser()

    private val token = "test-a1b2c3"

    @BeforeEach
    fun setup() {
        entityManager.persist(user)
    }

    @AfterEach
    fun tearDown() {
        entityManager.clear()
    }

    @Test
    fun `when new token gets stored then check if it can be retrieved`() {

        val tokenEntity = VerificationToken(
            token = token,
            appUser = user,
            expiryDate = Instant.now().plus(1, ChronoUnit.DAYS)
        )

        entityManager.persist(tokenEntity)

        val foundToken: VerificationToken? = objectUnderTest.findByToken(token)

        assertNotNull(foundToken)
        assertEquals(token, foundToken?.token)
    }

    @Test
    fun `when new token doesn't exist then check if it can be retrieved`() {
        val foundToken: VerificationToken? = objectUnderTest.findByToken(token)

        assertNull(foundToken)
    }
}
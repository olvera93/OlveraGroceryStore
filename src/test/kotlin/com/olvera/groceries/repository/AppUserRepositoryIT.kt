package com.olvera.groceries.repository

import com.olvera.groceries.model.AppUser
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class AppUserRepositoryIT @Autowired constructor(
    val entityManager: TestEntityManager,
    val objectUnderTest: AppUserRepository
) {
    private val userEmail = "olvera.gom@example.com"
    private val userName = "Gonzalo-olvera"
    private val user = AppUser(
        firstName = "Gonzalo",
        lastName = "Olvera",
        email = userEmail,
        appUsername = userName,
        clientPassword = "SecurePassword1234!",
        isVerified = true,
    )

    @BeforeEach
    fun setup() {
        entityManager.persist(user)
    }

    @AfterEach
    fun tearDown() {
        entityManager.clear()
    }

    @Test
    fun `when new user gets persisted than check if email can be found`() {

        val actualUser: AppUser? = objectUnderTest.findByEmail(userEmail)

        assertNotNull(actualUser)
        assertEquals(userEmail, actualUser?.email)
    }

    @Test
    fun `when user email does not exist then check is user is null`() {

        val fakeEmail = "none-existing-user@example.com"

        val actualUser: AppUser? = objectUnderTest.findByEmail(fakeEmail)

        assertNull(actualUser)
    }

    @Test
    fun `when new user gets persisted than check if username can be found`() {

        val actualUser: AppUser? = objectUnderTest.findByAppUsername(userName)

        assertNotNull(actualUser)
        assertEquals(userName, actualUser?.appUsername)

    }

    @Test
    fun `when user username does not exist then check is user is null`() {
        val fakeUserName = "none-existing"

        val actualUser: AppUser? = objectUnderTest.findByAppUsername(fakeUserName)

        assertNull(actualUser)
    }

    @Test
    fun `when find user by username and email is called then check if the actual responses have the same field values`() {

        val actualUserByUsername = objectUnderTest.findByAppUsername(userName)
        val actualUserByEmail = objectUnderTest.findByEmail(userEmail)

        assertEquals(actualUserByUsername?.authorities, actualUserByEmail?.authorities)
        assertEquals(actualUserByUsername?.password, actualUserByEmail?.password)
        assertEquals(actualUserByUsername?.username, actualUserByEmail?.username)
        assertEquals(actualUserByUsername?.isEnabled, actualUserByEmail?.isEnabled)
        assertEquals(actualUserByUsername?.isAccountNonExpired, actualUserByEmail?.isAccountNonExpired)
        assertEquals(actualUserByUsername?.isAccountNonLocked, actualUserByEmail?.isAccountNonLocked)
        assertEquals(actualUserByUsername?.isCredentialsNonExpired, actualUserByEmail?.isCredentialsNonExpired)
        assertEquals(actualUserByUsername?.isVerified, actualUserByEmail?.isVerified)

    }
}
package com.olvera.groceries.service

import com.olvera.groceries.error.SignUpException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.service.impl.EmailServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.mail.javamail.JavaMailSender

class EmailServiceImplTest {

    private val mockMailSender = mockk<JavaMailSender>(relaxed = true)

    private val mockMimeMessage = mockk<MimeMessage>(relaxed = true)

    private val token = "a1-b2-c3-d4"

    private val user = AppUser(
        email = "zeyne@mtv.jd",
        clientPassword = "asef",
        firstName = "Zeyne",
        lastName = "Al khoury",
        appUsername = "zeyne-urduniyya"
    )

    private val objectUnderTest = EmailServiceImpl(mockMailSender)

    private val password = "top-secret-pw"


    @Test
    fun `when send verification email is called then expect email successfully send`() {
        every { mockMailSender.createMimeMessage() } returns mockMimeMessage
        every { mockMailSender.send(mockMimeMessage) } returns Unit

        objectUnderTest.sendVerificationEmail(user, token)

        verify { mockMailSender.createMimeMessage() }
        verify { mockMailSender.send(mockMimeMessage) }

    }

    @Test
    fun `when send verification email is called then expect failed to send email message`() {
        val expectedException = SignUpException("Failed to send email")

        every { mockMailSender.createMimeMessage() } throws expectedException

        val actualResult: SignUpException = assertThrows { objectUnderTest.sendVerificationEmail(user, token) }

        assertEquals(expectedException.message, actualResult.message)

        verify { mockMailSender.createMimeMessage() }
        verify(exactly = 0) { mockMailSender.send(mockMimeMessage) }

    }

    @Test
    fun `when send password reset email is called then expect email successfully send`() {

        every { mockMailSender.createMimeMessage() } returns mockMimeMessage
        every { mockMailSender.send(mockMimeMessage) } returns Unit

        objectUnderTest.sendPasswordResetEmail(user, password)

        verify { mockMailSender.createMimeMessage() }
        verify { mockMailSender.send(mockMimeMessage) }

    }

    @Test
    fun `when send password reset email is called then expect failed to send email message`() {
        val expectedException = SignUpException("Failed to send email")

        every { mockMailSender.send(any<MimeMessage>()) } throws expectedException

        val actualResult: SignUpException = assertThrows { objectUnderTest.sendPasswordResetEmail(user, password) }

        assertEquals(expectedException.message, actualResult.message)

        verify { mockMailSender.createMimeMessage() }
        verify { mockMailSender.send(any<MimeMessage>()) }

    }
}
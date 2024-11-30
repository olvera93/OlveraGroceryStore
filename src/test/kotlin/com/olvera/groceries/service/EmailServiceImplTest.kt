package com.olvera.groceries.service

import com.olvera.groceries.model.AppUser
import com.olvera.groceries.service.impl.EmailServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.Test
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

    @Test
    fun `when send verification email is called then expect email successfully send`() {
        every { mockMailSender.createMimeMessage() } returns mockMimeMessage
        every { mockMailSender.send(mockMimeMessage) } returns Unit

        objectUnderTest.sendVerificationEmail(user, token)

        verify { mockMailSender.createMimeMessage() }
        verify { mockMailSender.send(mockMimeMessage) }


    }



}
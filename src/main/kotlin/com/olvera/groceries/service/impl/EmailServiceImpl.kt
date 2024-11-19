package com.olvera.groceries.service.impl

import com.olvera.groceries.error.SignUpException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.service.EmailService
import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(private val mailSender: JavaMailSender) : EmailService {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun sendVerificationEmail(appUser: AppUser, token: String) {
        val link = "http://localhost:9000/api/v1/auth/verify?token=${token}"
        val verificationText = getVerificationEmailText(link)
        sendEmail(appUser, "Confirm your email for olvera-coding.com", verificationText)
    }


    override fun sendPasswordResetEmail(appUser: AppUser, newPassword: String) {
        val resetEmailText = "<p>This is your new password: $newPassword</p>"
        sendEmail(appUser, "Password reset", resetEmailText)
    }

    fun getVerificationEmailText(link: String): String {
        return """
        <p style="Margin:0 0 20px 0; font-size:19px; line-height:25px; color:#0b0c0c;">
            Thank you for registering. Please click on the link below to activate your account:
        </p>
        <blockquote style="Margin:0 0 20px 0; border-left:10px solid #b1b4b6; padding:15px; font-size:16px;">
            <a href="$link" style="color:#1a73e8; text-decoration:none;">Activate your account</a>
        </blockquote>
        <p style="Margin:0 0 20px 0; font-size:16px; line-height:24px; color:#0b0c0c;">
            If you did not register, please ignore this email.
        </p>
    """.trimIndent()

    }

    private fun buildEmail(name: String, text: String): String {
        return """
        <html>
            <head>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        margin: 0;
                        padding: 20px;
                        background-color: #f4f7fa;
                    }
                    .email-container {
                        max-width: 600px;
                        margin: 0 auto;
                        background-color: #ffffff;
                        padding: 20px;
                        border-radius: 8px;
                        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                    }
                    h1 {
                        color: #333333;
                        font-size: 24px;
                    }
                    p {
                        font-size: 16px;
                        line-height: 1.5;
                        color: #555555;
                    }
                    .footer {
                        margin-top: 20px;
                        font-size: 14px;
                        color: #777777;
                    }
                </style>
            </head>
            <body>
                <div class="email-container">
                    <h1>Hello, $name!</h1>
                    <p>$text</p>
                    <div class="footer">
                        <p>If you have any questions, feel free to contact us.</p>
                    </div>
                </div>
            </body>
        </html>
    """.trimIndent()
    }


    private fun sendEmail(appUser: AppUser, subject: String, content: String) {
        try {
            val mimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, "utf-8")
            helper.setText(buildEmail(appUser.firstName, content), true)
            helper.setTo(appUser.email)
            helper.setSubject(subject)
            helper.setFrom("olvera.groceries@gmail.com")
            mailSender.send(mimeMessage)
        } catch (me: MailException) {
            logger.error("Failed to send email: ${me.message}")
            throw SignUpException("Failed to send email")
        }
    }
}
package com.olvera.groceries.service.impl

import com.olvera.groceries.dto.AuthenticationRequest
import com.olvera.groceries.dto.AuthenticationResponse
import com.olvera.groceries.dto.EmailConfirmedResponse
import com.olvera.groceries.dto.RegisterRequest
import com.olvera.groceries.error.AccountVerificationException
import com.olvera.groceries.error.SignUpException
import com.olvera.groceries.error.TokenExpiredException
import com.olvera.groceries.error.UsernamePasswordMismatchException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.model.VerificationToken
import com.olvera.groceries.repository.AppUserRepository
import com.olvera.groceries.repository.VerificationTokenRepository
import com.olvera.groceries.service.AccountManagementService
import com.olvera.groceries.service.EmailService
import com.olvera.groceries.service.JwtService
import com.olvera.groceries.util.SignUpMapper
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Service
class AccountManagementServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val mapper: SignUpMapper,
    private val userRepository: AppUserRepository,
    private val tokenRepository: VerificationTokenRepository,
    private val emailService: EmailService
) : AccountManagementService {

    companion object {
        private const val TEN_CHARACTERS = 10
    }

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun signUp(request: RegisterRequest): EmailConfirmedResponse {
        checkForSignUpMistakes(request)

        val user: AppUser = mapper.toEntity(request, passwordEncoder)
        val savedUser = userRepository.save(user)
        val (token, verificationToken) = initiateEmailVerification(savedUser)

        tokenRepository.save(verificationToken)
        emailService.sendVerificationEmail(savedUser, token)

        return EmailConfirmedResponse("Please, check your email and spam/junk for ${user.email} to verify your account.")

    }

    override fun verify(token: String): EmailConfirmedResponse {
        val currentVerificationToken =
            tokenRepository.findByToken(token) ?: throw AccountVerificationException("Invalid Token!")

        val user = currentVerificationToken.appUser
        if (currentVerificationToken.isExpired()) {
            log.error("Token expired for user: $user")
            currentVerificationToken.token = UUID.randomUUID().toString()
            currentVerificationToken.expiryDate = Instant.now().plus(15, ChronoUnit.MINUTES)
            tokenRepository.save(currentVerificationToken)
            emailService.sendVerificationEmail(user, currentVerificationToken.token)
            throw TokenExpiredException("Token expired, a new verification link has been sent to your email: ${user.email}")
        }

        if (user.isVerified) {
            log.error("Account already verified for: $user")
            throw AccountVerificationException("Account is already verified for: $user")
        }

        user.isVerified = true
        userRepository.save(user)

        return EmailConfirmedResponse("Account successfully verified.")
    }

    @Transactional
    override fun signIn(request: AuthenticationRequest): AuthenticationResponse {
        val user =
            userRepository.findByAppUsername(request.username) ?: throw UsernameNotFoundException("User not found!")

        if (!user.isVerified) {
            log.error("User not verified: $user")
            throw AccountVerificationException("You did not click on the verification link yet, check your mails for ${user.email}")
        }

        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))
        } catch (bce: BadCredentialsException) {
            log.error("Username or password is incorrect: ${bce.message}")
            throw UsernamePasswordMismatchException("Username or password is incorrect")
        }

        val accessToken: String = jwtService.generateAccessToken(user)
        val refreshToken: String = jwtService.generateRefreshToken(user)

        return AuthenticationResponse(accessToken, refreshToken)
    }

    override fun resetPassword(email: String): EmailConfirmedResponse {
        val user = userRepository.findByEmail(email) ?: throw UsernameNotFoundException("Email: $email does not exist!")

        val newPassword = UUID.randomUUID().toString().take(TEN_CHARACTERS)
        user.clientPassword = passwordEncoder.encode(newPassword)
        userRepository.save(user)

        emailService.sendPasswordResetEmail(user, newPassword)

        return EmailConfirmedResponse("New password sent to $email successfully.")

    }

    private fun initiateEmailVerification(appUser: AppUser): Pair<String, VerificationToken> {
        val token = UUID.randomUUID().toString()
        val verificationToken = VerificationToken(
            token = token,
            appUser = appUser,
            expiryDate = Instant.now().plus(15, ChronoUnit.MINUTES)
        )

        return Pair(token, verificationToken)
    }

    private fun checkForSignUpMistakes(request: RegisterRequest) {
        userRepository.findByEmail(request.email)?.let {
            log.error("User email already exists: ${request.email}")
            throw SignUpException("User email already exists!")
        }

        userRepository.findByAppUsername(request.username)?.let {
            log.error("Username already exists: ${request.username}")
            throw SignUpException("Username already exists!")
        }

        if (request.password != request.passwordConfirmation) {
            log.error("Password and password confirmation does not match: $request")
            throw SignUpException("Password and password confirmation does not match!")
        }
    }
}
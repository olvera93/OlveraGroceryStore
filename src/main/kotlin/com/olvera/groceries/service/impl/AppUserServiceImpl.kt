package com.olvera.groceries.service.impl

import com.olvera.groceries.dto.UserInfoResponse
import com.olvera.groceries.dto.UserInfoUpdateRequest
import com.olvera.groceries.dto.UserPasswordUpdateRequest
import com.olvera.groceries.error.BadRequestException
import com.olvera.groceries.error.PasswordMismatchException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.repository.AppUserRepository
import com.olvera.groceries.service.AppUserService
import com.olvera.groceries.service.ClientSessionService
import com.olvera.groceries.util.UserInfoMapper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AppUserServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val repository: AppUserRepository,
    private val mapper: UserInfoMapper,
    private val service: ClientSessionService
) : AppUserService {

    override fun changeEmail(request: Map<String, String>): UserInfoResponse {
        val currentUser: AppUser = service.findCurrentSessionUser()

        val newEmail = request["email"] ?: throw BadRequestException("The new email is missing in the request")
        validateEmail(newEmail, currentUser.id)

        currentUser.email = newEmail
        val updatedUser = repository.save(currentUser)

        return mapper.toDto(updatedUser)
    }

    override fun changePassword(request: UserPasswordUpdateRequest) {
        val user: AppUser = service.findCurrentSessionUser()

        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw PasswordMismatchException("The current password is wrong!")
        }

        if (request.newPassword != request.newPasswordConfirmation) {
            throw PasswordMismatchException("Your new password does not match with the password confirmation!")
        }

        user.clientPassword = passwordEncoder.encode(request.newPassword)
        repository.save(user)

    }

    override fun changeInfo(request: UserInfoUpdateRequest): UserInfoResponse {
        val user: AppUser = service.findCurrentSessionUser()

        user.apply {
            this.firstName = request.firstName ?: firstName
            this.lastName = request.lastName ?: lastName
        }

        val savedUser: AppUser = repository.save(user)
        return mapper.toDto(savedUser)
    }

    override fun fetchInfo(): UserInfoResponse {
        val user: AppUser = service.findCurrentSessionUser()
        return mapper.toDto(user)
    }

    fun validateEmail(email: String, currentUserId: Long) {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$"
        if (!email.matches(emailRegex.toRegex())) {
            throw BadRequestException("Invalid email format")
        }

        repository.findByEmail(email)?.let { user ->
            if (user.id != currentUserId) {
                throw BadRequestException("Email is already used by another user")
            }
        }
    }
}
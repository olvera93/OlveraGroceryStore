package com.olvera.groceries.service.impl

import com.olvera.groceries.error.UserNotFoundException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.repository.AppUserRepository
import com.olvera.groceries.repository.VerificationTokenRepository
import com.olvera.groceries.service.ClientSessionService
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class ClientSessionServiceImpl(
    private val repository: AppUserRepository
) : ClientSessionService {


    override fun retrieveAuthentication(): Authentication? {
        return SecurityContextHolder.getContext().authentication
    }

    override fun findCurrentSessionUser(): AppUser {
        val authentication: Authentication =
            retrieveAuthentication() ?: throw UserNotFoundException("Authenticated user not found")

        val username = when (val principal = authentication.principal) {
            is UserDetails -> principal.username
            else -> principal.toString()
        }

        return repository.findByAppUsername(username) ?: throw UserNotFoundException("User $username not found")

    }

    override fun getAuthenticatedUser(): AppUser {
        val authentication: Authentication? = retrieveAuthentication()
        return authentication?.principal as AppUser
    }
}
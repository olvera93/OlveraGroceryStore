package com.olvera.groceries.service

import com.olvera.groceries.model.AppUser
import org.springframework.security.core.Authentication

interface ClientSessionService {

    fun retrieveAuthentication(): Authentication?

    fun findCurrentSessionUser(): AppUser

    fun getAuthenticatedUser(): AppUser
}
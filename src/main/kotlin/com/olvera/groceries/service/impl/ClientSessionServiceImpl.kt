package com.olvera.groceries.service.impl

import com.olvera.groceries.model.AppUser
import com.olvera.groceries.service.ClientSessionService
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

@Service
class ClientSessionServiceImpl : ClientSessionService {
    override fun retrieveAuthentication(): Authentication {
        TODO("Not yet implemented")
    }

    override fun findCurrentSessionUser(): AppUser {
        TODO("Not yet implemented")
    }

    override fun getAuthenticatedUser(): AppUser {
        TODO("Not yet implemented")
    }
}
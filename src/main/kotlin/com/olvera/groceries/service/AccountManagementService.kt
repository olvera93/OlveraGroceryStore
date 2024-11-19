package com.olvera.groceries.service

import com.olvera.groceries.dto.AuthenticationRequest
import com.olvera.groceries.dto.AuthenticationResponse
import com.olvera.groceries.dto.EmailConfirmedResponse
import com.olvera.groceries.dto.RegisterRequest

interface AccountManagementService {

    fun signUp(request: RegisterRequest): EmailConfirmedResponse

    fun verify(token: String): EmailConfirmedResponse

    fun signIn(request: AuthenticationRequest): AuthenticationResponse

    fun resetPassword(email: String): EmailConfirmedResponse

}
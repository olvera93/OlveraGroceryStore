package com.olvera.groceries.service

import com.olvera.groceries.model.AppUser

interface EmailService {

    fun sendVerificationEmail(appUser: AppUser, token: String)

    fun sendPasswordResetEmail(appUser: AppUser, newPassword: String)

}
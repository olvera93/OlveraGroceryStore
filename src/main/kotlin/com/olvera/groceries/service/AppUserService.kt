package com.olvera.groceries.service

import com.olvera.groceries.dto.UserInfoResponse
import com.olvera.groceries.dto.UserInfoUpdateRequest
import com.olvera.groceries.dto.UserPasswordUpdateRequest

interface AppUserService {

    fun changeEmail(request: Map<String, String>): UserInfoResponse

    fun changePassword(request: UserPasswordUpdateRequest)

    fun changeInfo(request: UserInfoUpdateRequest): UserInfoResponse

    fun fetchInfo(): UserInfoResponse

}
package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class UserPasswordUpdateRequest(

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("currentPassword", required = true)
    val currentPassword: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("newPassword", required = true)
    val newPassword: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("newPasswordConfirmation", required = true)
    val newPasswordConfirmation: kotlin.String,
)

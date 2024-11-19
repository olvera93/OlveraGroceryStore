package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class AuthenticationRequest(
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("username", required = true)
    val username: String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("password", required = true)
    val password: String
)

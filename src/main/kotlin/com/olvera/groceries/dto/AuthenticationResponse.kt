package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class AuthenticationResponse(

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("accessToken", required = true)
    val accessToken: String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("refreshToken", required = true)
    val refreshToken: String
)

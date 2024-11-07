package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserInfoResponse(

    @get:Pattern(regexp = "\\w+")
    @get:Size(max = 30)
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("firstName", required = true)
    val firstName: kotlin.String,

    @get:Pattern(regexp = "\\w+")
    @get:Size(max = 30)
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("lastName", required = true)
    val lastName: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("email", required = true)
    val email: kotlin.String,

    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("username", required = true)
    val username: kotlin.String,
)

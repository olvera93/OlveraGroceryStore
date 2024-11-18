package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class RegisterRequest(

    @get:Pattern(regexp = "\\w+")
    @get:Size(max = 30)
    @Schema(example = "", required = true, description = "")
    @get:JsonProperty("firstName")
    val firstName: String,

    @get:Pattern(regexp = "\\w+")
    @get:Size(max = 30)
    @Schema(example = "", required = true, description = "")
    @get:JsonProperty("lastName")
    val lastName: String,


    @Schema(example = "", required = true, description = "")
    @get:JsonProperty("email")
    val email: String,

    @Schema(example = "", required = true, description = "")
    @get:JsonProperty("username")
    val username: String,

    @Schema(example = "", required = true, description = "")
    @get:JsonProperty("password")
    val password: String,

    @Schema(example = "", required = true, description = "")
    @get:JsonProperty("passwordConfirmation")
    val passwordConfirmation: String

)

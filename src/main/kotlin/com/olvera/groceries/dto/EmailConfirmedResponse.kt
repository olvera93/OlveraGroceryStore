package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class EmailConfirmedResponse(

    @Schema(example = "null", required = true, description = "Email confirmed")
    @get:JsonProperty("message", required = true)
    val message: String
)

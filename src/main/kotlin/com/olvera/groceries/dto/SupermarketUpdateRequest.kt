package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class SupermarketUpdateRequest(

    @Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("market") val market: Hypermarket? = null,
)

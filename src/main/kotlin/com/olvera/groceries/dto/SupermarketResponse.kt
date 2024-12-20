package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class SupermarketResponse(

    @Schema(example = "null", description = "Unique identifier for the supermarket")
    @get:JsonProperty("id") val id: Long? = null,

    @Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("market") val market: Hypermarket? = null,
)

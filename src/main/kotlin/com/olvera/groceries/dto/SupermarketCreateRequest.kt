package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class SupermarketCreateRequest(

    @Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("market", required = true) val market: Hypermarket,
)

package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class ShoppingListUpdateRequest(

    @Schema(example = "null", description = "Status indicating if the shopping list is completed")
    @get:JsonProperty("isDone") val isDone: Boolean? = null,

    @field:Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("supermarket") val supermarket: SupermarketUpdateRequest? = null
)
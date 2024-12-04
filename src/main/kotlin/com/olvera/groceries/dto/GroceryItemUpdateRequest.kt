package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class GroceryItemUpdateRequest(

    @Schema(example = "null", description = "Name of the grocery item")
    @get:JsonProperty("name") val name: String? = null,

    @Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("category") val category: Category? = null,

    )

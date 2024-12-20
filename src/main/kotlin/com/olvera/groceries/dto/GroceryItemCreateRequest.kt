package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class GroceryItemCreateRequest(

    @Schema(example = "null", required = true, description = "Name of the item")
    @get:JsonProperty("name", required = true) val name: String,

    @Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("category", required = true) val category: Category,
)

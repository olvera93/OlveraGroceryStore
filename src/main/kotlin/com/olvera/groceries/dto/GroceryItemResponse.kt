package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class GroceryItemResponse(

    @Schema(example = "null", description = "Unique identifier for the GroceryItem")
    @get:JsonProperty("id") val id: Long? = null,

    @Schema(example = "null", description = "The name of the grocery item")
    @get:JsonProperty("name") val name: String? = null,

    @Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("category") val category: Category? = null,


    )

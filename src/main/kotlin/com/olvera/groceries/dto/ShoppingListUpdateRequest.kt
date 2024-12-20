package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class ShoppingListUpdateRequest(

    @Schema(example = "null", description = "Quantity of grocery items")
    @get:JsonProperty("quantity") val quantity: Int? = null,

    @Schema(example = "", description = "Price of the grocery items")
    @get:JsonProperty("price") val price: Float? = null,

    @Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("groceryItem") val groceryItem: GroceryItemUpdateRequest? = null,
)
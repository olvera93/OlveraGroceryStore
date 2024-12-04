package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class ShoppingListItemCreateRequest(

    @Schema(example = "null", required = true, description = "Amount of grocery items")
    @get:JsonProperty("quantity", required = true) val quantity: Int,

    @Schema(example = "null", required = true, description = "Price of the item")
    @get:JsonProperty("price", required = true) val price: Float,

    @Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("groceryItem", required = true) val groceryItem: GroceryItemCreateRequest,
)

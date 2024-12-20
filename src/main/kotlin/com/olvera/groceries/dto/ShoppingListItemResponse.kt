package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class ShoppingListItemResponse(

    @Schema(example = "null", required = true, description = "Unique identifier for the ShoppingListItem")
    @get:JsonProperty("id", required = true) val id: Long,

    @Schema(example = "null", required = true, description = "Quantity of the grocery item")
    @get:JsonProperty("quantity", required = true) val quantity: Int,

    @Schema(example = "null", required = true, description = "Price of the grocery item")
    @get:JsonProperty("price", required = true) val price: Float,

    @Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("groceryItem", required = true) val groceryItem: GroceryItemResponse
)

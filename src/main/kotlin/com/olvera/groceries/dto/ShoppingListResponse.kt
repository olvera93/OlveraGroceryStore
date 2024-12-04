package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class ShoppingListResponse(

    @Schema(example = "null", description = "Unique identifier for this ShoppingList")
    @get:JsonProperty("id") val id: Long? = null,

    @Schema(example = "null", description = "Indicates if the shopping list is completed")
    @get:JsonProperty("isDone") val isDone: Boolean? = null,

    @Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("supermarket") val supermarket: SupermarketResponse? = null,

    @Valid
    @Schema(example = "null", description = "")
    @get:JsonProperty("shoppingListItems") val shoppingListItems: List<ShoppingListItemResponse>? = null,

    @Schema(example = "null", description = "Total amount of the shopping list")
    @get:JsonProperty("totalAmount") val totalAmount: Float? = null

)

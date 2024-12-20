package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid

data class ShoppingListCreateRequest(

    @Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("supermarket", required = true) val supermarket: SupermarketCreateRequest,

    @Valid
    @Schema(example = "null", required = true, description = "")
    @get:JsonProperty("shoppingListItems", required = true) val shoppingListItems: List<ShoppingListItemCreateRequest>,

)

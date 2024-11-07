package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty

enum class Category(val value: String) {

    @JsonProperty("FRUITS") FRUITS("FRUITS"),
    @JsonProperty("CEREAL") CEREAL("CEREAL"),
    @JsonProperty("VEGETABLES") VEGETABLES("VEGETABLES"),
    @JsonProperty("CANNED_GOODS") CANNED_GOODS("CANNED_GOODS"),
    @JsonProperty("DAIRY") DAIRY("DAIRY"),
    @JsonProperty("MEAT") MEAT("MEAT"),
    @JsonProperty("FISH") FISH("FISH"),
    @JsonProperty("SEAFOOD") SEAFOOD("SEAFOOD"),
    @JsonProperty("DELI") DELI("DELI"),
    @JsonProperty("SPICES") SPICES("SPICES"),
    @JsonProperty("BREAD") BREAD("BREAD"),
    @JsonProperty("BAKERY") BAKERY("BAKERY"),
    @JsonProperty("BEVERAGES") BEVERAGES("BEVERAGES"),
    @JsonProperty("PASTA") PASTA("PASTA"),
    @JsonProperty("RICE") RICE("RICE"),
    @JsonProperty("BAKING") BAKING("BAKING"),
    @JsonProperty("SNACKS") SNACKS("SNACKS"),
    @JsonProperty("SWEET") SWEET("SWEET"),
    @JsonProperty("FROZEN_FOOD") FROZEN_FOOD("FROZEN_FOOD"),
    @JsonProperty("ICE_CREAM") ICE_CREAM("ICE_CREAM"),
    @JsonProperty("HOUSEHOLD_SUPPLIES") HOUSEHOLD_SUPPLIES("HOUSEHOLD_SUPPLIES"),
    @JsonProperty("PET_FOOD") PET_FOOD("PET_FOOD"),
    @JsonProperty("OTHER") OTHER("OTHER")
}
package com.olvera.groceries.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UserInfoUpdateRequest(

    @get:Pattern(regexp = "\\w+")
    @get:Size(max = 30)
    @Schema(example = "null", description = "")
    @get:JsonProperty("firstName")
    val firstName: kotlin.String? = null,

    @get:Pattern(regexp = "\\w+")
    @get:Size(max = 30)
    @Schema(example = "null", description = "")
    @get:JsonProperty("lastName")
    val lastName: kotlin.String? = null

)

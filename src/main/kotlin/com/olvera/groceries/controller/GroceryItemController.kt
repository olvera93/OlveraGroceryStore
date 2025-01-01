package com.olvera.groceries.controller

import com.olvera.groceries.dto.GroceryItemResponse
import com.olvera.groceries.dto.GroceryItemUpdateRequest
import com.olvera.groceries.error.ApiError
import com.olvera.groceries.service.ClientSessionService
import com.olvera.groceries.service.ShoppingListService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/v1"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
@CrossOrigin(value = ["*"])
@Validated
@Tag(name = "grocery-item-resource")
class GroceryItemController(
    private val clientSessionService: ClientSessionService,
    private val shoppingListService: ShoppingListService
) {


    @Operation(
        summary = "Get grocery item",
        description = "Method to fetch a grocery item or inform not found",
        operationId = "getGroceryItem"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Found the grocery item",
            content = [Content(schema = Schema(implementation = GroceryItemResponse::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Grocery item not found ",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            ), ApiResponse(
                responseCode = "400",
                description = "Invalid Input",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            ), ApiResponse(
                responseCode = "500",
                description = "Error with the server",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            )]
    )
    @GetMapping(value = ["/shopping-lists/{listId}/shopping-list-items/{itemId}/grocery-item"])
    fun getGroceryItem(
        @Parameter(description = "", required = true)
        @PathVariable("listId") listId: Long,
        @Parameter(description = "", required = true)
        @PathVariable("itemId") itemId: Long
    ): ResponseEntity<GroceryItemResponse> {
        return ResponseEntity.ok(
            shoppingListService.getGroceryItem(
                listId,
                itemId,
                clientSessionService.getAuthenticatedUser()
            )
        )
    }

    @Operation(
        summary = "Update grocery item",
        description = "Method to update a grocery item or inform not found",
        operationId = "updateGroceryItem"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Update the grocery item successfully",
            content = [Content(schema = Schema(implementation = GroceryItemResponse::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Grocery item not found ",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            ), ApiResponse(
                responseCode = "400",
                description = "Invalid Input",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            ), ApiResponse(
                responseCode = "500",
                description = "Error with the server",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            )]
    )
    @PatchMapping(value = ["/shopping-lists/{listId}/shopping-list-items/{itemId}/grocery-item/{groceryId}"])
    fun updateGroceryItem(
        @Parameter(description = "", required = true)
        @PathVariable("listId") listId: Long,
        @Parameter(description = "", required = true)
        @PathVariable("itemId") itemId: Long,
        @Parameter(description = "", required = true)
        @PathVariable("groceryId") groceryId: Long,
        @Parameter(description = "", required = true)
        @Valid @RequestBody groceryItemUpdateRequest: GroceryItemUpdateRequest
    ): ResponseEntity<GroceryItemResponse> {
        val groceryItem = shoppingListService.updateGroceryItem(groceryId, groceryItemUpdateRequest)
        return ResponseEntity.ok(groceryItem)
    }
}
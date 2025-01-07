package com.olvera.groceries.controller

import com.olvera.groceries.dto.ShoppingListItemCreateRequest
import com.olvera.groceries.dto.ShoppingListItemResponse
import com.olvera.groceries.dto.ShoppingListItemUpdateRequest
import com.olvera.groceries.dto.ShoppingListResponse
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
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/v1"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
@CrossOrigin(value = ["*"])
@Validated
@Tag(name = "shopping-list-item-resource")
class ShoppingListItemController(
    private val clientSessionService: ClientSessionService,
    private val shoppingListService: ShoppingListService
) {

    @Operation(
        summary = "Create a new shopping list item",
        description = "Method to create a shopping list item or inform about invalid input",
        operationId = "createShoppingListItem"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "shopping list item created successfully",
            content = [Content(schema = Schema(implementation = ShoppingListResponse::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Not found ",
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
    @PostMapping(value = ["/shopping-lists/{listId}/shopping-list-items"])
    fun createShoppingListItem(
        @Parameter(description = "", required = true)
        @PathVariable("listId") listId: Long,
        @Parameter(description = "", required = true)
        @Valid @RequestBody shoppingListItemCreateRequest: ShoppingListItemCreateRequest
    ): ResponseEntity<ShoppingListItemResponse> {
        val listItemResponse = shoppingListService.createShoppingListItem(
            listId,
            shoppingListItemCreateRequest,
            clientSessionService.getAuthenticatedUser()
        )
        return ResponseEntity(listItemResponse, HttpStatus.CREATED)
    }

    @Operation(
        summary = "Delete shopping list item by the ID",
        description = "Method to delete a shopping list item by the ID or inform not found",
        operationId = "deleteShoppingListItemById"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "204",
            description = "Deleted the shopping list item by the supplied ID",
            content = [Content(schema = Schema(implementation = ApiError::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Not found ",
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
    @DeleteMapping(value = ["/shopping-lists/{listId}/shopping-list-items/{itemId}"])
    fun deleteShoppingListItemById(
        @Parameter(description = "", required = true)
        @PathVariable("listId") listId: Long,
        @Parameter(description = "", required = true)
        @PathVariable("itemId") itemId: Long
    ): ResponseEntity<Unit> {
        shoppingListService.deleteShoppingListItem(listId, itemId, clientSessionService.getAuthenticatedUser())
        return ResponseEntity(Unit, HttpStatus.NO_CONTENT)
    }

    @Operation(
        summary = "Get shopping list item by the ID",
        description = "Method to fetch a shopping list item by the ID or inform not found",
        operationId = "getShoppingListItemById"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Found the shopping list item by the supplied ID",
            content = [Content(schema = Schema(implementation = ShoppingListItemResponse::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Shopping list item not found ",
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
    @GetMapping(value = ["/shopping-lists/{listId}/shopping-list-items/{itemId}"])
    fun getShoppingListItemById(
        @Parameter(description = "", required = true)
        @PathVariable("listId") listId: Long,
        @Parameter(description = "", required = true)
        @PathVariable("itemId") itemId: Long
    ): ResponseEntity<ShoppingListItemResponse> {
        return ResponseEntity.ok(
            shoppingListService.getShoppingListItem(
                listId,
                itemId,
                clientSessionService.getAuthenticatedUser()
            )
        )
    }


    @Operation(
        summary = "Get shopping list items",
        description = "Method to fetch a shopping list items or inform not found",
        operationId = "getShoppingListItems"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Found the shopping list items by the supplied list ID",
            content = [Content(schema = Schema(implementation = ShoppingListItemResponse::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Shopping list items not found ",
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
    @GetMapping(value = ["/shopping-lists/{listId}/shopping-list-items"])
    fun getShoppingListItems(
        @Parameter(description = "", required = true)
        @PathVariable("listId") listId: Long
    ): ResponseEntity<List<ShoppingListItemResponse>> {
        val listItemResponse: List<ShoppingListItemResponse> =
            shoppingListService.getShoppingListItems(listId, clientSessionService.getAuthenticatedUser()).toList()
        return ResponseEntity.ok(listItemResponse)
    }

    @Operation(
        summary = "Update shopping list item by the ID",
        description = "Method to update a shopping list item by the ID or inform not found",
        operationId = "updateShoppingListItemById"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Updated the shopping list item by the supplied ID",
            content = [Content(schema = Schema(implementation = ShoppingListItemResponse::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Shopping list item not found ",
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
    @PatchMapping(value = ["/shopping-lists/{listId}/shopping-list-items/{itemId}"])
    fun updateShoppingListItemById(
        @Parameter(description = "", required = true)
        @PathVariable("listId") listId: Long,
        @Parameter(description = "", required = true)
        @PathVariable("itemId") itemId: Long,
        @Parameter(description = "", required = true)
        @Valid @RequestBody shoppingListUpdateRequest: ShoppingListItemUpdateRequest
    ): ResponseEntity<ShoppingListItemResponse> {
        val listItemResponse = shoppingListService.updateShoppingListItem(
            listId,
            itemId,
            shoppingListUpdateRequest,
            clientSessionService.getAuthenticatedUser()
        )
        return ResponseEntity.ok(listItemResponse)
    }
}
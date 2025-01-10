package com.olvera.groceries.controller

import com.olvera.groceries.dto.ShoppingListCreateRequest
import com.olvera.groceries.dto.ShoppingListResponse
import com.olvera.groceries.dto.ShoppingListUpdateRequest
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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/v1"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
@CrossOrigin(value = ["*"])
@Validated
@Tag(name = "shopping-list-resource")
class ShoppingListController(
    private val clientSessionService: ClientSessionService,
    private val shoppingListService: ShoppingListService
) {

    @Operation(
        summary = "Create a new shopping list",
        description = "Method to create a new shopping list or inform about invalid input",
        operationId = "createShoppingList"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "shopping list created successfully",
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
    @PostMapping(value = ["/shopping-lists"])
    fun createShoppingList(
        @Parameter(description = "", required = true)
        @Valid @RequestBody shoppingListCreateRequest: ShoppingListCreateRequest
    ): ResponseEntity<ShoppingListResponse> {
        val shoppingList = shoppingListService.createShoppingList(
            shoppingListCreateRequest,
            clientSessionService.getAuthenticatedUser()
        )
        return ResponseEntity(shoppingList, HttpStatus.CREATED)
    }

    @Operation(
        summary = "Remove shopping list by its ID",
        description = "Method to delete a shopping list by the ID or inform about not found",
        operationId = "deleteShoppingList"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "204",
            description = "Shopping list deleted successfully"
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
    @DeleteMapping(value = ["/shopping-list/{id}"])
    fun deleteShoppingList(
        @Parameter(description = "", required = true)
        @PathVariable("id") id: Long
    ): ResponseEntity<Unit> {
        shoppingListService.deleteShoppingList(id, clientSessionService.getAuthenticatedUser())
        return ResponseEntity(Unit, HttpStatus.NO_CONTENT)
    }

    @Operation(
        summary = "Get shopping list by its ID",
        description = "Method to fetch a shopping list by the ID or inform about not found",
        operationId = "getShoppingListById"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Found the shopping list by the supplied ID",
            content = [Content(schema = Schema(implementation = ShoppingListResponse::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Shopping list not found",
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
    @GetMapping(value = ["/shopping-lists/{id}"])
    fun getShoppingListById(
        @Parameter(description = "", required = true)
        @PathVariable("id") id: Long
    ): ResponseEntity<ShoppingListResponse> =
        ResponseEntity.ok(shoppingListService.getShoppingListById(id, clientSessionService.getAuthenticatedUser()))


    @Operation(
        summary = "Get all shopping lists",
        description = "Method to fetch all shoppings lists, both open and closed",
        operationId = "getShoppingLists"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "All open & closed shopping lists",
            content = [Content(schema = Schema(implementation = ShoppingListResponse::class))]
        ),
            ApiResponse(
                responseCode = "500",
                description = "Error with the server",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            )]
    )
    @GetMapping(value = ["/shopping-lists"])
    fun getShoppingLists(
        @Parameter(description = "")
        @Valid @RequestParam(value = "is-done", required = false) isDone: Boolean?
    ): ResponseEntity<List<ShoppingListResponse>> {
        val shoppingLists =
            shoppingListService.getShoppingLists(clientSessionService.getAuthenticatedUser(), isDone).toList()
        return ResponseEntity.ok(shoppingLists)
    }

    @Operation(
        summary = "Update shopping list by its ID",
        description = "Method to update a shopping list by the ID or inform about not found",
        operationId = "updateShoppingList"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Updated the shopping list by the supplied ID",
            content = [Content(schema = Schema(implementation = ShoppingListResponse::class))]
        ),
            ApiResponse(
                responseCode = "404",
                description = "Shopping list not found",
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
    @PatchMapping(value = ["/shopping-lists/{id}"])
    fun updateShoppingList(
        @Parameter(description = "", required = true)
        @PathVariable("id") id: Long,
        @Parameter(description = "", required = true)
        @Valid @RequestBody shoppingListUpdateRequest: ShoppingListUpdateRequest
    ): ResponseEntity<ShoppingListResponse> {
        val shoppingList = shoppingListService.updateShoppingList(
            id,
            shoppingListUpdateRequest,
            clientSessionService.getAuthenticatedUser()
        )
        return ResponseEntity.ok(shoppingList)
    }


}
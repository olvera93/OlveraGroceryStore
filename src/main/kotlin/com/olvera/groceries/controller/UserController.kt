package com.olvera.groceries.controller

import com.olvera.groceries.dto.UserInfoResponse
import com.olvera.groceries.dto.UserInfoUpdateRequest
import com.olvera.groceries.dto.UserPasswordUpdateRequest
import com.olvera.groceries.error.ApiError
import com.olvera.groceries.service.AppUserService
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
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(path = ["/api/v1"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
@CrossOrigin(value = ["*"])
@Validated
@Tag(name = "user-resource")

class UserController(
    private val userService: AppUserService
) {

    @Operation(summary = "Change user email", description = "Method to change user email")
    @ApiResponses(
        value = [ApiResponse(responseCode = "200", description = "Updating the email was successful"), ApiResponse(
            responseCode = "404",
            description = "Not found ",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        ), ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        ), ApiResponse(
            responseCode = "500",
            description = "Error with the server",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        )]
    )
    @PatchMapping(value = ["/user/email"])
    fun changeEmail(
        @Parameter(
            description = "",
            required = true
        ) @Valid @RequestBody requestBody: Map<String, String>
    ): ResponseEntity<UserInfoResponse> =
        ResponseEntity.ok(userService.changeEmail(requestBody))

    @Operation(
        summary = "Change user information",
        description = "Method to change user information like, name, username, etc.."
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Updating the information was successful"
        ), ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        ), ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        ), ApiResponse(
            responseCode = "500",
            description = "Error with the server",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        )]
    )
    @PatchMapping(value = ["/user/info"])
    fun changeInfo(
        @Parameter(
            description = "",
            required = true
        ) @Valid @RequestBody userInfoUpdateRequest: UserInfoUpdateRequest
    ): ResponseEntity<UserInfoResponse> = ResponseEntity.ok(userService.changeInfo(userInfoUpdateRequest))

    @Operation(
        summary = "Change user password",
        description = "Method to change user password"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Updating the password was successful"
        ), ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        ), ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        ), ApiResponse(
            responseCode = "500",
            description = "Error with the server",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        )]
    )
    @PatchMapping(value = ["/user/password"])
    fun changePassword(
        @Parameter(
            description = "",
            required = true
        ) @Valid @RequestBody userPasswordUpdateRequest: UserPasswordUpdateRequest
    ) = ResponseEntity.ok(userService.changePassword(userPasswordUpdateRequest))


    @Operation(
        summary = "Fetch user information",
        description = "Method to get all persisted information from the current logged-in user"
    )
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Fetching the user information was successful"
        ), ApiResponse(
            responseCode = "404",
            description = "Not found",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        ), ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        ), ApiResponse(
            responseCode = "500",
            description = "Error with the server",
            content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
        )]
    )
    @GetMapping(value = ["/user"])
    fun fetchInfo(): ResponseEntity<UserInfoResponse> = ResponseEntity.ok(userService.fetchInfo())


}
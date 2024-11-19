package com.olvera.groceries.controller

import com.olvera.groceries.dto.AuthenticationRequest
import com.olvera.groceries.dto.AuthenticationResponse
import com.olvera.groceries.dto.EmailConfirmedResponse
import com.olvera.groceries.dto.RegisterRequest
import com.olvera.groceries.error.ApiError
import com.olvera.groceries.service.AccountManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.jetbrains.annotations.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/v1/auth"], produces = [(MediaType.APPLICATION_JSON_VALUE)])
@CrossOrigin(value = ["*"])
@Validated
@Tag(name = "authentication-resource")
class AuthenticationController(
    private val accountManagementService: AccountManagementService
) {

    @Operation(
        summary = "reset user password",
        operationId = "resetPassword",
        description = "Method to reset user password or inform about reset problem"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "resetting password was successful",
                content = [Content(schema = Schema(implementation = EmailConfirmedResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Could not reset password",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error with the server",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            )]
    )
    @PostMapping(value = ["/password-reset"])
    fun resetPassword(
        @NotNull
        @Parameter(
            description = "",
            required = true
        ) @Valid @RequestParam(value = "email", required = true) email: String
    ): ResponseEntity<EmailConfirmedResponse> =
        ResponseEntity.ok(accountManagementService.resetPassword(email))


    @Operation(
        summary = "Sign-in user",
        operationId = "signIn",
        description = "Method to sign-in user or inform about sign-in problems"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Sign-in was successful",
                content = [Content(schema = Schema(implementation = AuthenticationResponse::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Invalid sign-in",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error with the server",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            )]
    )
    @PostMapping(value = ["/sign-in"])
    fun signIn(
        @Parameter(
            description = "",
            required = true
        ) @Valid @RequestBody authenticationRequest: AuthenticationRequest
    ): ResponseEntity<AuthenticationResponse> =
        ResponseEntity.ok(accountManagementService.signIn(authenticationRequest))


    @Operation(
        summary = "Sign-up an user",
        operationId = "signUp",
        description = "Method to sign-up user or inform about sign-up problems"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Sign-up was successful",
                content = [Content(schema = Schema(implementation = EmailConfirmedResponse::class))]
            ),
            ApiResponse(
                responseCode = "409",
                description = "Invalid sign-up attempt",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error with the server",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            )]
    )
    @PostMapping(value = ["/sign-up"])
    fun signUp(
        @Parameter(
            description = "",
            required = true
        ) @Valid @RequestBody registerRequest: RegisterRequest
    ): ResponseEntity<EmailConfirmedResponse> {
        val response = accountManagementService.signUp(registerRequest)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @Operation(
        summary = "Verify user",
        operationId = "verify",
        description = "Method to verify user or inform about verification problem"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Email verification was was successful",
                content = [Content(schema = Schema(implementation = EmailConfirmedResponse::class))]
            ),
            ApiResponse(
                responseCode = "403",
                description = "Verification step not completed",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            ),
            ApiResponse(
                responseCode = "500",
                description = "Error with the server",
                content = arrayOf(Content(schema = Schema(implementation = ApiError::class)))
            )]
    )
    @GetMapping(value = ["/verify"])
    fun verify(
        @NotNull @Parameter(description = "", required = true) @Valid @RequestParam(
            value = "token",
            required = true
        ) token: String
    ): ResponseEntity<EmailConfirmedResponse> =
        ResponseEntity.ok(accountManagementService.verify(token))

}
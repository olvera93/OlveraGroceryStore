package com.olvera.groceries.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.olvera.groceries.dto.AuthenticationRequest
import com.olvera.groceries.dto.AuthenticationResponse
import com.olvera.groceries.dto.EmailConfirmedResponse
import com.olvera.groceries.dto.RegisterRequest
import com.olvera.groceries.service.AccountManagementService
import com.olvera.groceries.service.JwtService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [AuthenticationController::class])
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mockJwtService: JwtService

    @MockBean
    private lateinit var mockAccountManagementsService: AccountManagementService

    private val successResponse = EmailConfirmedResponse("success response")

    private val mapper = jacksonObjectMapper()

    @Test
    fun `when user sign up is called then expect success message`() {
        val registerRequest = RegisterRequest(
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@gmail.com",
            username = "johndoe",
            password = "ahlan",
            passwordConfirmation = "ahlan"
        )

        `when`(mockAccountManagementsService.signUp(registerRequest)).thenReturn(successResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(registerRequest))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(successResponse.message))
    }

    @Test
    fun `when user verify is called then expect success message`() {
        val token = "a1b2c3d4"

        `when`(mockAccountManagementsService.verify(token)).thenReturn(successResponse)


        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", token)
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(successResponse.message))
    }

    @Test
    fun `when user sign in is called then expect matching access and refresh token`() {
        val request = AuthenticationRequest(username = "johndoe", password = "ahlan")
        val response = AuthenticationResponse(accessToken = "a1b2c3d4", refreshToken = "z9x8y7")
        `when`(mockAccountManagementsService.signIn(request)).thenReturn(response)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value(response.accessToken))
            .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").value(response.refreshToken))
    }

    @Test
    fun `when user reset password is called then expect success message`() {
        val email = "johndoe@gmail.com"
        `when`(mockAccountManagementsService.resetPassword(email)).thenReturn(successResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/auth/password-reset")
                .contentType(MediaType.APPLICATION_JSON)
                .param("email", email)
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(successResponse.message))

    }


}
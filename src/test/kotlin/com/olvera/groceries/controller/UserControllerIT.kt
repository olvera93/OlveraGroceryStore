package com.olvera.groceries.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.olvera.groceries.dto.UserInfoResponse
import com.olvera.groceries.dto.UserInfoUpdateRequest
import com.olvera.groceries.dto.UserPasswordUpdateRequest
import com.olvera.groceries.service.AppUserService
import com.olvera.groceries.service.ClientSessionService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testUser", roles = ["USER"])
class UserControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var mockSessionService: ClientSessionService

    @MockBean
    private lateinit var mockUserService: AppUserService

    private val mapper = jacksonObjectMapper()

    val userResponse = UserInfoResponse(
        firstName = "John",
        lastName = "Smith",
        email = "john.smith@gmail.com",
        username = "jonhsmith"
    )

    @Test
    fun `when change email endpoints is called then return http 200 status`() {

        val email = "olvera@gmail.com"

        val infoResponse = UserInfoResponse(
            firstName = "John",
            lastName = "Smith",
            email = email,
            username = "jonhsmith"
        )

        val request = HashMap<String, String>()
        request["email"] = email
        `when`(mockUserService.changeEmail(request)).thenReturn(infoResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/user/email")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(infoResponse.email))
    }

    @Test
    fun `when change info endpoint is called then return http 200 status and check changed first and last name`() {
        val updateRequest = UserInfoUpdateRequest(firstName = "Paco", lastName = "Sanchez")

        val updateResponse = UserInfoResponse(
            firstName = updateRequest.firstName!!,
            lastName = updateRequest.lastName!!,
            email = "john.smith@gmail.com",
            username = "jonhsmith"
        )

        `when`(mockUserService.changeInfo(updateRequest)).thenReturn(updateResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/user/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateResponse))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(updateRequest.firstName))
            .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(updateRequest.lastName))
    }

    @Test
    fun `when change password endpoint is called then return http 200 status`() {
        val passwordRequest = UserPasswordUpdateRequest(
            currentPassword = "oldPassword",
            newPassword = "newPassword",
            newPasswordConfirmation = "newPassword"
        )

        Mockito.doNothing().`when`(mockUserService).changePassword(passwordRequest)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/v1/user/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(passwordRequest))
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `when fetch info endpoint is called then return http 200 status and check response fields`() {
        `when`(mockUserService.fetchInfo()).thenReturn(userResponse)

        val resultActions: ResultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
        )

        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userResponse.email))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userResponse.username))
        .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(userResponse.firstName))
        .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(userResponse.lastName))

    }

}
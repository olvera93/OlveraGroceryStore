package com.olvera.groceries.controller

import com.olvera.groceries.service.ClientSessionService
import org.springframework.web.bind.annotation.RestController

@RestController
class ShoppingListItemController(
    private val clientSessionService: ClientSessionService,
) {
}
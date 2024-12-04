package com.olvera.groceries.service

import com.olvera.groceries.dto.Hypermarket
import com.olvera.groceries.model.Supermarket

interface SupermarketService {

    fun findSupermarketByName(name: Hypermarket): Supermarket

}
package com.olvera.groceries.util

import com.olvera.groceries.dto.SupermarketCreateRequest
import com.olvera.groceries.dto.SupermarketResponse
import com.olvera.groceries.model.Supermarket
import org.springframework.stereotype.Component

@Component
class SupermarketMapper {

    fun toDto(entity: Supermarket): SupermarketResponse {
        return SupermarketResponse(
            id = entity.id,
            market = entity.name
        )
    }

    fun toEntity(dto: SupermarketCreateRequest): Supermarket {
        return Supermarket(name = dto.market)
    }
}
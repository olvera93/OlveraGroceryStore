package com.olvera.groceries.util

import com.olvera.groceries.dto.GroceryItemCreateRequest
import com.olvera.groceries.dto.GroceryItemResponse
import com.olvera.groceries.model.GroceryItem
import org.springframework.stereotype.Component

@Component
class GroceryItemMapper {

    fun toDto(entity: GroceryItem) = GroceryItemResponse(
        id = entity.id,
        name = entity.name,
        category = entity.category
    )

    fun toEntity(dto: GroceryItemCreateRequest) = GroceryItem(
        name = dto.name,
        category = dto.category
    )
}
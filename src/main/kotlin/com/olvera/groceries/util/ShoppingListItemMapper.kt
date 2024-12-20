package com.olvera.groceries.util

import com.olvera.groceries.dto.ShoppingListItemCreateRequest
import com.olvera.groceries.dto.ShoppingListItemResponse
import com.olvera.groceries.model.GroceryItem
import com.olvera.groceries.model.ShoppingList
import com.olvera.groceries.model.ShoppingListItem
import org.springframework.stereotype.Component

@Component
class ShoppingListItemMapper(private val mapper: GroceryItemMapper) {

    fun toDto(entity: ShoppingListItem?): ShoppingListItemResponse {
        val groceryItemResponse  = mapper.toDto(entity?.groceryItem!!)
        val dto = ShoppingListItemResponse(
            id = entity.id,
            quantity = entity.quantity,
            price = entity.price,
            groceryItem = groceryItemResponse
        )

        return dto
    }

    fun toEntity(
        request: ShoppingListItemCreateRequest,
        shoppingList: ShoppingList?,
        groceryItem: GroceryItem
    ): ShoppingListItem {
        val entity = ShoppingListItem(
            quantity = request.quantity,
            price = request.price,
            shoppingList = shoppingList,
            groceryItem = groceryItem
        )

        return entity
    }

}
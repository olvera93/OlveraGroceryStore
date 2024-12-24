package com.olvera.groceries.util

import com.olvera.groceries.dto.ShoppingListItemCreateRequest
import com.olvera.groceries.dto.ShoppingListItemResponse
import com.olvera.groceries.dto.ShoppingListResponse
import com.olvera.groceries.dto.SupermarketResponse
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.model.ShoppingList
import com.olvera.groceries.model.ShoppingListItem
import com.olvera.groceries.model.Supermarket
import org.springframework.stereotype.Component

@Component
class ShoppingListMapper {

    fun toDto(
        entity: ShoppingList,
        supermarket: SupermarketResponse,
        listItems: List<ShoppingListItemResponse>
    ): ShoppingListResponse {

        val dto = ShoppingListResponse(
            id = entity.id,
            isDone = entity.isDone,
            supermarket = supermarket,
            totalAmount = entity.getTotalAmount(),
        )

        return dto
    }

    fun toEntity(
        request: ShoppingListItemCreateRequest,
        supermarket: Supermarket,
        listItems: List<ShoppingListItem>,
        user: AppUser
    ): ShoppingList {
        val entity = ShoppingList(
            isDone = false,
            supermarket = supermarket,
            shoppingListItems = listItems,
            appUser = user
        )
        return entity
    }

}
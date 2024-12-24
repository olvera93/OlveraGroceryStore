package com.olvera.groceries.service

import com.olvera.groceries.dto.ShoppingListItemCreateRequest
import com.olvera.groceries.dto.ShoppingListItemUpdateRequest
import com.olvera.groceries.model.ShoppingList
import com.olvera.groceries.model.ShoppingListItem

interface ShoppingListItemService {

    fun createShoppingListItem(request: ShoppingListItemCreateRequest, shoppingList: ShoppingList): ShoppingListItem

    fun getShoppingListItem(id: Long): ShoppingListItem

    fun deleteShoppingListItem(id: Long)

    fun deleteShoppingListItems(shoppingListItems: List<ShoppingListItem>)

    fun updateShoppingListItem(id: Long, request: ShoppingListItemUpdateRequest): ShoppingListItem

    fun updateShoppingListItems(shoppingList: ShoppingList, shoppingListItems: List<ShoppingListItem>): List<ShoppingListItem>

    fun updateShoppingList(shoppingList: ShoppingList, shoppingListItems: List<ShoppingListItem>): List<ShoppingListItem>


}
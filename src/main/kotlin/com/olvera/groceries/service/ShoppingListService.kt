package com.olvera.groceries.service

import com.olvera.groceries.dto.*
import com.olvera.groceries.model.AppUser

interface ShoppingListService {

    fun createShoppingList(request: ShoppingListCreateRequest, user: AppUser): ShoppingListResponse

    fun getShoppingListById(id: Long, user: AppUser): ShoppingListResponse

    fun getShoppingLists(user: AppUser, isDone: Boolean?): Set<ShoppingListResponse>

    fun updateShoppingList(id: Long, request: ShoppingListUpdateRequest, user: AppUser): ShoppingListResponse

    fun deleteShoppingList(id: Long, user: AppUser)

    fun getGroceryItem(listId: Long, itemId: Long, user: AppUser): GroceryItemResponse

    fun updateGroceryItem(id: Long, request: GroceryItemUpdateRequest): GroceryItemResponse

    fun getShoppingListItems(listId: Long, user: AppUser): Set<ShoppingListItemResponse>

    fun updateShoppingListItem(listId: Long, itemId: Long, user: AppUser): ShoppingListItemResponse

    fun createShoppingListItem(
        listId: Long,
        request: ShoppingListItemCreateRequest,
        user: AppUser
    ): ShoppingListItemResponse

    fun deleteShoppingListItem(listId: Long, itemId: Long, user: AppUser)

    fun updateShoppingListItem(
        listId: Long,
        itemId: Long,
        request: ShoppingListItemUpdateRequest,
        user: AppUser
    ): ShoppingListItemResponse
}
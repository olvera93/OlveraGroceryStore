package com.olvera.groceries.service

import com.olvera.groceries.dto.GroceryItemCreateRequest
import com.olvera.groceries.dto.GroceryItemResponse
import com.olvera.groceries.dto.GroceryItemUpdateRequest
import com.olvera.groceries.model.GroceryItem

interface GroceryItemService {

    fun deleteGroceryItems(groceries: List<GroceryItem>)

    fun createGroceryItem(request: GroceryItemCreateRequest): GroceryItemResponse

    fun deleteGroceryItem(id: Long)

    fun updateGroceryItem(id: Long, request: GroceryItemUpdateRequest): GroceryItemResponse

}
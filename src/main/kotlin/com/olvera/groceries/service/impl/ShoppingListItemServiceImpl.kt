package com.olvera.groceries.service.impl

import com.olvera.groceries.dto.ShoppingListItemCreateRequest
import com.olvera.groceries.dto.ShoppingListItemUpdateRequest
import com.olvera.groceries.model.GroceryItem
import com.olvera.groceries.model.ShoppingList
import com.olvera.groceries.model.ShoppingListItem
import com.olvera.groceries.repository.ShoppingListItemRepository
import com.olvera.groceries.service.GroceryItemService
import com.olvera.groceries.service.ShoppingListItemService
import com.olvera.groceries.util.GroceryItemMapper
import org.springframework.stereotype.Service

@Service
class ShoppingListItemServiceImpl(
    private val repository: ShoppingListItemRepository,
    private val mapper: GroceryItemMapper,
    private val service: GroceryItemService
) : ShoppingListItemService {

    override fun createShoppingListItem(
        request: ShoppingListItemCreateRequest,
        shoppingList: ShoppingList
    ): ShoppingListItem {

        val groceryItem = mapper.toEntity(request.groceryItem)
        val listItem = ShoppingListItem(
            quantity = request.quantity,
            price = request.price,
            shoppingList = shoppingList,
            groceryItem = groceryItem
        )
        return repository.save(listItem)
    }

    override fun getShoppingListItem(id: Long): ShoppingListItem {
        return repository.findById(id).orElseThrow()
    }

    override fun deleteShoppingListItem(id: Long) {
        repository.deleteShoppingListItem(id)
    }

    override fun deleteShoppingListItems(shoppingListItems: List<ShoppingListItem>) {
        val groceryItems: List<GroceryItem> = shoppingListItems.mapNotNull { it.groceryItem }
        repository.deleteAll(shoppingListItems)
        if (groceryItems.isNotEmpty()) {
            service.deleteGroceryItems(groceryItems)
        }
    }

    override fun updateShoppingListItem(id: Long, request: ShoppingListItemUpdateRequest): ShoppingListItem {
        val listItem = repository.findById(id).orElseThrow()
        listItem.apply {
            this.quantity = request.quantity ?: this.quantity
            this.price = request.price ?: this.price
        }

        val groceryId = listItem.groceryItem?.id
        if (groceryId != null) {
            request.groceryItem?.let { updateRequest -> service.updateGroceryItem(groceryId, updateRequest) }
        }

        return repository.save(listItem)
    }

    override fun updateShoppingListItems(
        shoppingList: ShoppingList,
        shoppingListItems: List<ShoppingListItem>
    ): List<ShoppingListItem> {
        shoppingListItems.forEach { it.shoppingList = shoppingList }
        return repository.saveAllAndFlush(shoppingListItems)
    }
}
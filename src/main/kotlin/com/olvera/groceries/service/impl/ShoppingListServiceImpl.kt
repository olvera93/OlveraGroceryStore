package com.olvera.groceries.service.impl

import com.olvera.groceries.dto.*
import com.olvera.groceries.error.BadRequestException
import com.olvera.groceries.error.ShoppingListNotFoundException
import com.olvera.groceries.error.SupermarketException
import com.olvera.groceries.model.AppUser
import com.olvera.groceries.model.ShoppingList
import com.olvera.groceries.model.ShoppingListItem
import com.olvera.groceries.model.Supermarket
import com.olvera.groceries.repository.ShoppingListRepository
import com.olvera.groceries.service.GroceryItemService
import com.olvera.groceries.service.ShoppingListItemService
import com.olvera.groceries.service.ShoppingListService
import com.olvera.groceries.service.SupermarketService
import com.olvera.groceries.util.ShoppingListItemMapper
import com.olvera.groceries.util.ShoppingListMapper
import com.olvera.groceries.util.SupermarketMapper
import org.springframework.stereotype.Service

@Service
class ShoppingListServiceImpl(
    private val shoppingListMapper: ShoppingListMapper,
    private val shoppingListRepository: ShoppingListRepository,
    private val supermarketMapper: SupermarketMapper,
    private val supermarketService: SupermarketService,
    private val shoppingListItemMapper: ShoppingListItemMapper,
    private val shoppingListItemService: ShoppingListItemService,
    private val groceryItemService: GroceryItemService
) : ShoppingListService {

    override fun createShoppingList(request: ShoppingListCreateRequest, user: AppUser): ShoppingListResponse {
        val supermarket: Supermarket = supermarketService.findSupermarketByName(request.supermarket.market)
        val shoppingList = createThenSaveShoppingList(request, supermarket, user)
        val listItems = createThenAttachShoppingListItems(request, shoppingList)
        return generateShoppingListResponse(supermarket, listItems, shoppingList)
    }

    override fun getShoppingListById(id: Long, user: AppUser): ShoppingListResponse {
        val shoppingList = findUserShoppingList(id, user)
        val supermarketResponse = shoppingList.supermarket?.let { supermarketMapper.toDto(it) }
            ?: throw SupermarketException("There is no supermarket associated with shopping list!")
        val listItemResponses = shoppingList.shoppingListItems.map { item -> shoppingListItemMapper.toDto(item) }
        return shoppingListMapper.toDto(shoppingList, supermarketResponse, listItemResponses)
    }

    override fun getShoppingLists(user: AppUser, isDone: Boolean?): Set<ShoppingListResponse> {
        TODO("Not yet implemented")
    }

    override fun updateShoppingList(id: Long, request: ShoppingListUpdateRequest, user: AppUser): ShoppingListResponse {
        TODO("Not yet implemented")
    }

    override fun deleteShoppingList(id: Long, user: AppUser) {
        TODO("Not yet implemented")
    }

    override fun getGroceryItem(listId: Long, listItemId: Long, user: AppUser): GroceryItemResponse {
        TODO("Not yet implemented")
    }

    override fun updateGroceryItem(id: Long, request: GroceryItemUpdateRequest): GroceryItemResponse {
        TODO("Not yet implemented")
    }

    override fun getShoppingListItems(listId: Long, user: AppUser): Set<ShoppingListItemResponse> {
        TODO("Not yet implemented")
    }

    override fun updateShoppingListItem(listId: Long, itemId: Long, user: AppUser): ShoppingListItemResponse {
        TODO("Not yet implemented")
    }

    override fun updateShoppingListItem(
        listId: Long,
        itemId: Long,
        request: ShoppingListItemUpdateRequest,
        user: AppUser
    ): ShoppingListResponse {
        TODO("Not yet implemented")
    }

    override fun createShoppingListItem(
        listId: Long,
        request: ShoppingListItemCreateRequest,
        user: AppUser
    ): ShoppingListItemResponse {
        TODO("Not yet implemented")
    }

    override fun deleteShoppingListItem(listId: Long, itemId: Long, user: AppUser) {
        TODO("Not yet implemented")
    }

    private fun validateRequest(request: ShoppingListCreateRequest) {
        if (request.shoppingListItems.isEmpty()) {
            throw BadRequestException("A shopping list must have at least one item")
        }
    }

    private fun createThenSaveShoppingList(
        request: ShoppingListCreateRequest,
        supermarket: Supermarket,
        user: AppUser
    ): ShoppingList {

        val shoppingList = shoppingListMapper.toEntity(request, supermarket, emptyList(), user)
        return shoppingListRepository.save(shoppingList)
    }

    private fun createThenAttachShoppingListItems(
        request: ShoppingListCreateRequest,
        shoppingList: ShoppingList
    ): List<ShoppingListItem> {
        val listItems = request.shoppingListItems.map { item ->
            shoppingListItemService.createShoppingListItem(item, shoppingList)
        }
        return shoppingListItemService.updateShoppingList(shoppingList, listItems)
    }

    private fun generateShoppingListResponse(
        supermarket: Supermarket,
        listItems: List<ShoppingListItem>,
        entity: ShoppingList
    ): ShoppingListResponse {
        val supermarketResponse = supermarketMapper.toDto(supermarket)
        val shoppingListItemResponses: List<ShoppingListItemResponse> =
            listItems.map { item -> shoppingListItemMapper.toDto(item) }
        val listResponse = shoppingListMapper.toDto(entity, supermarketResponse, shoppingListItemResponses)
        shoppingListItemResponses.forEach { listItems.map { item -> shoppingListItemMapper.toDto(item) } }
        return listResponse
    }

    private fun findUserShoppingList(id: Long, user: AppUser): ShoppingList {
        val shoppingList = shoppingListRepository.findByIdAndUser(id, user)
            ?: throw ShoppingListNotFoundException("Shopping list with ID: $id does not exist")
        return shoppingList
    }
}
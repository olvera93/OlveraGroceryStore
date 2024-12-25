package com.olvera.groceries.service.impl

import com.olvera.groceries.dto.*
import com.olvera.groceries.error.BadRequestException
import com.olvera.groceries.error.ShoppingListItemNotFoundException
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
import jakarta.transaction.Transactional
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
        validateRequest(request)
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
        val shoppingLists = fetchShoppingLists(user, isDone)
        return shoppingLists.map(this::mapToShoppingListResponse).toSet()
    }

    @Transactional
    override fun updateShoppingList(id: Long, request: ShoppingListUpdateRequest, user: AppUser): ShoppingListResponse {
        val shoppingList = findUserShoppingList(id, user)
        val updatedSupermarket: Supermarket? = if (request.supermarket != null) {
            request.supermarket.market?.let { supermarketName ->
                supermarketService.findSupermarketByName(
                    supermarketName
                )
            }
        } else {
            shoppingList.supermarket ?: Supermarket(name = Hypermarket.OTHER)
        }
        shoppingList.apply {
            this.isDone = request.isDone ?: this.isDone
            this.supermarket = updatedSupermarket ?: this.supermarket
        }
        val entity: ShoppingList = shoppingListRepository.save(shoppingList)
        return generateShoppingListResponse(
            updatedSupermarket!!, shoppingList.shoppingListItems, entity
        )
    }

    @Transactional
    override fun deleteShoppingList(id: Long, user: AppUser) {
        val shoppingList = findUserShoppingList(id, user)
        shoppingListItemService.deleteShoppingListItems(shoppingList.shoppingListItems)
        shoppingListRepository.delete(shoppingList)
    }

    override fun getGroceryItem(listId: Long, itemId: Long, user: AppUser): GroceryItemResponse {
        val listResponse: ShoppingListResponse = getShoppingListById(listId, user)
        val itemListResponse: ShoppingListItemResponse = retrieveListItemResponse(listResponse, itemId)
        return itemListResponse.groceryItem
    }

    override fun updateGroceryItem(id: Long, request: GroceryItemUpdateRequest): GroceryItemResponse {
        val groceryItemResponse = groceryItemService.updateGroceryItem(id, request)
        return groceryItemResponse
    }

    override fun getShoppingListItems(listId: Long, user: AppUser): Set<ShoppingListItemResponse> {
        val shoppingList: ShoppingList = findUserShoppingList(listId, user)
        return shoppingList.shoppingListItems.map(shoppingListItemMapper::toDto).toSet()
    }

    override fun updateShoppingListItem(listId: Long, itemId: Long, user: AppUser): ShoppingListItemResponse {
        val shoppingList = getShoppingListById(listId, user)
        return retrieveListItemResponse(shoppingList, itemId)
    }

    override fun updateShoppingListItem(
        listId: Long,
        itemId: Long,
        request: ShoppingListItemUpdateRequest,
        user: AppUser
    ): ShoppingListItemResponse {
        val listItem = shoppingListItemService.updateShoppingListItem(itemId, request)
        return shoppingListItemMapper.toDto(listItem)
    }

    override fun createShoppingListItem(
        listId: Long,
        request: ShoppingListItemCreateRequest,
        user: AppUser
    ): ShoppingListItemResponse {
        val shoppingList = findUserShoppingList(listId, user)
        val listItem = shoppingListItemService.createShoppingListItem(request, shoppingList)
        return shoppingListItemMapper.toDto(listItem)
    }

    @Transactional
    override fun deleteShoppingListItem(listId: Long, itemId: Long, user: AppUser) {
        val shoppingList = shoppingListRepository.findByIdAndUser(listId, user)
        val listItem = shoppingList?.shoppingListItems?.find { listItem -> listItem.id == itemId }
            ?: throw ShoppingListItemNotFoundException("Shopping list item with ID: $itemId not found!")
        shoppingListItemService.deleteShoppingListItem(itemId)
        listItem.groceryItem?.let { groceryItemService.deleteGroceryItem(it.id) }
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

    private fun fetchShoppingLists(user: AppUser, isDone: Boolean?): List<ShoppingList> {
        return if (isDone != null) {
            shoppingListRepository.findAllByUserAndIsDone(user, isDone) ?: emptyList()
        } else {
            shoppingListRepository.findAllByAppUser(user) ?: emptyList()
        }
    }

    private fun mapToShoppingListResponse(shoppingList: ShoppingList): ShoppingListResponse {
        val supermarketResponse = shoppingList.supermarket?.let(supermarketMapper::toDto) ?: SupermarketResponse()
        val listItems = shoppingList.shoppingListItems.map(shoppingListItemMapper::toDto)
        return shoppingListMapper.toDto(shoppingList, supermarketResponse, listItems)
    }

    private fun retrieveListItemResponse(
        listResponse: ShoppingListResponse,
        listItemId: Long
    ): ShoppingListItemResponse {
        return listResponse.shoppingListItems?.firstOrNull { itemResponse -> itemResponse.id == listItemId }
            ?: throw BadRequestException("Shopping list item with ID: $listItemId does not exist")
    }
}
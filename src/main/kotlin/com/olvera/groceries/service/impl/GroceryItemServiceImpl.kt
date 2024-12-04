package com.olvera.groceries.service.impl

import com.olvera.groceries.dto.GroceryItemCreateRequest
import com.olvera.groceries.dto.GroceryItemResponse
import com.olvera.groceries.dto.GroceryItemUpdateRequest
import com.olvera.groceries.model.GroceryItem
import com.olvera.groceries.repository.GroceryItemRepository
import com.olvera.groceries.service.GroceryItemService
import com.olvera.groceries.util.GroceryItemMapper
import org.springframework.stereotype.Service

@Service
class GroceryItemServiceImpl(
    private val groceryItemRepository: GroceryItemRepository,
    private val mapper: GroceryItemMapper
) : GroceryItemService {

    override fun deleteGroceryItems(groceries: List<GroceryItem>) {
        groceryItemRepository.deleteAll(groceries)
    }

    override fun createGroceryItem(request: GroceryItemCreateRequest): GroceryItemResponse {
        val item: GroceryItem =
            groceryItemRepository.save(GroceryItem(name = request.name, category = request.category))
        return mapper.toDto(item)
    }

    override fun deleteGroceryItem(id: Long) {
        groceryItemRepository.deleteGroceryItem(id)
    }

    override fun updateGroceryItem(id: Long, request: GroceryItemUpdateRequest): GroceryItemResponse {
        val entity: GroceryItem = groceryItemRepository.findById(id).orElseThrow()
        entity.apply {
            this.name = request.name ?: this.name
            this.category = request.category ?: this.category
        }

        val updatedEntity: GroceryItem = groceryItemRepository.save(entity)

        return mapper.toDto(updatedEntity)
    }
}
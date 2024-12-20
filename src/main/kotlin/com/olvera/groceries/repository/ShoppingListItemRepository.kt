package com.olvera.groceries.repository

import com.olvera.groceries.model.ShoppingListItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListItemRepository : JpaRepository<ShoppingListItem, Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ShoppingListItem i WHERE i.id = ?1")
    fun deleteShoppingListItem(id: Long)

}
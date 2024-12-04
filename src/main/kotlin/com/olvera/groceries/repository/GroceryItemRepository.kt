package com.olvera.groceries.repository

import com.olvera.groceries.model.GroceryItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface GroceryItemRepository: JpaRepository<GroceryItem, Long> {

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM GroceryItem g WHERE g.id = ?1")
    fun deleteGroceryItem(id: Long)

}
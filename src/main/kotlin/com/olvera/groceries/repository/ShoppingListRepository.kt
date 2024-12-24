package com.olvera.groceries.repository

import com.olvera.groceries.model.AppUser
import com.olvera.groceries.model.ShoppingList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListRepository: JpaRepository<ShoppingList, Long> {

    @Query("SELECT s FROM ShoppingList s WHERE s.appUser = ?1")
    fun findAllByAppUser(user: AppUser): List<ShoppingList>?

    @Query("SELECT s FROM ShoppingList s WHERE s.id = ?1 AND s.appUser = ?2")
    fun findByIdAndUser(id: Long, user: AppUser): ShoppingList?

    @Query("SELECT s FROM ShoppingList s WHERE s.appUser = ?1 AND s.isDone = ?2")
    fun findAllByUserAndIsDone(user: AppUser, isDone: Boolean): List<ShoppingList>?
}
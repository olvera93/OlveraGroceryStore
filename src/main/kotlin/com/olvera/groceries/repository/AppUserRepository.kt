package com.olvera.groceries.repository

import com.olvera.groceries.model.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AppUserRepository : JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u WHERE u.email = ?1")
    fun findByEmail(email: String): AppUser?

    @Query("SELECT u FROM AppUser u WHERE u.appUsername = ?1")
    fun findByAppUsername(username: String): AppUser?

}
package com.olvera.groceries.repository

import com.olvera.groceries.model.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface VerificationTokenRepository: JpaRepository<VerificationToken, Long> {

    @Query("SELECT v FROM VerificationToken v WHERE v.token = ?1")
    fun findByToken(token: String): VerificationToken?

}
package com.olvera.groceries.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "verification_token")
class VerificationToken(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verification_token_id_seq")
    @SequenceGenerator(
        name = "verification_token_id_seq",
        sequenceName = "verification_token_id_seq",
        allocationSize = 1
    )
    val id: Long = 0,

    var token: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "app_user_id")
    val appUser: AppUser,

    var expiryDate: Instant

) {

    fun isExpired(): Boolean = expiryDate.isBefore(Instant.now())

}
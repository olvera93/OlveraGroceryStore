package com.olvera.groceries.model

import com.olvera.groceries.dto.Hypermarket
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "supermarket")
class Supermarket(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "supermarket_id_seq")
    @SequenceGenerator(name = "supermarket_id_seq", sequenceName = "supermarket_id_seq", allocationSize = 1)
    val id: Long = 0,

    @NotNull
    @Enumerated(EnumType.STRING)
    var name: Hypermarket = Hypermarket.OTHER
)
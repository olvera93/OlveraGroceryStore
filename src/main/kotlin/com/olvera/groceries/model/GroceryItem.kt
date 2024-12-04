package com.olvera.groceries.model

import com.olvera.groceries.dto.Category
import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "grocery_item")
class GroceryItem (

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grocery_item_id_seq")
    @SequenceGenerator(
        name = "grocery_item_id_seq",
        sequenceName = "grocery_item_id_seq",
        allocationSize = 1
    )
    val id: Long = 0,

    @NotBlank
    var name: String,

    @NotNull
    @Enumerated(EnumType.STRING)
    var category: Category = Category.OTHER,

)
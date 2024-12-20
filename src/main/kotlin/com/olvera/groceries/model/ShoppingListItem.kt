package com.olvera.groceries.model

import jakarta.persistence.*

@Entity
@Table(name = "shopping_list_item")
class ShoppingListItem(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shopping_list_item_id_seq")
    @SequenceGenerator(
        name = "shopping_list_item_id_seq",
        sequenceName = "shopping_list_item_id_seq",
        allocationSize = 1
    )
    val id: Long = 0,

    var quantity: Int = 0,

    var price: Float = 0F,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shopping_list_id")
    var shoppingList: ShoppingList? = null,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "grocery_item_id")
    var groceryItem: GroceryItem? = null

)
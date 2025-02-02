package com.olvera.groceries.model

import jakarta.persistence.*

@Entity
@Table(name = "shopping_list")
class ShoppingList(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shopping_list_id_seq")
    @SequenceGenerator(name = "shopping_list_id_seq", sequenceName = "shopping_list_id_seq", allocationSize = 1)
    val id: Long = 0,

    var isDone: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    var appUser: AppUser? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supermarket_id")
    var supermarket: Supermarket? = null,

    @OneToMany(mappedBy = "shoppingList", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    var shoppingListItems: List<ShoppingListItem> = mutableListOf()

) {

    fun getTotalAmount(): Float {
        var totalAmount = 0f
        shoppingListItems.forEach { item -> totalAmount += item.price }
        return totalAmount
    }

}
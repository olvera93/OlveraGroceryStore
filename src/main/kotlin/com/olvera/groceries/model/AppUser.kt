package com.olvera.groceries.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "app.user")
class AppUser(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_id_seq")
    @SequenceGenerator(name = "app_user_id_seq", sequenceName = "app_user_id_seq", allocationSize = 1)
    val id: Long = 0,

    @NotBlank
    var firstName: String = "",

    @NotBlank
    var lastName: String = "",

    @NotBlank
    @Column(unique = true, nullable = false)
    var email: String = "",

    @NotBlank
    @Column(unique = true, nullable = false)
    var appUsername: String = "",

    @NotBlank
    var clientPassword: String = "",

    @NotNull
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,

    var isVerified: Boolean = false,

    @OneToOne(mappedBy = "appUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, optional = true)
    private val verificationToken: VerificationToken? = null,

    @OneToMany(mappedBy = "appUser", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private val shoppingList: List<ShoppingList>? = null,


) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String = clientPassword

    override fun getUsername(): String = appUsername

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = isVerified

}


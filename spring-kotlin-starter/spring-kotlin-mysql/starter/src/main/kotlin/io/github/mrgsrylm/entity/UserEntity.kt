package io.github.mrgsrylm.entity

import io.github.mrgsrylm.enums.Role
import io.github.mrgsrylm.enums.TokenClaims
import jakarta.persistence.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Entity
@Table(name = "USERS")
class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        var fullName: String,

        @Column(unique = true)
        var username: String,

        @Column(unique = true)
        var email: String,

        var password: String,

        @Enumerated(EnumType.STRING)
        @Column(name = "ROLE")
        var role: Role
) : BaseEntity(){

    fun getClaims(): Map<String, Any> {
        val claims = HashMap<String, Any>();
        claims[TokenClaims.ID.value] = this.id
        claims[TokenClaims.USER_FULL_NAME.value] = this.fullName
        claims[TokenClaims.USERNAME.value] = this.username
        claims[TokenClaims.EMAIL.value] = this.email
        claims[TokenClaims.ROLES.value] = this.role

        return claims;
    }

}
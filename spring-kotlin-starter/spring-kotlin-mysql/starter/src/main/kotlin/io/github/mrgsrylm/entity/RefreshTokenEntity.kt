package io.github.mrgsrylm.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "REFRESH_TOKEN")
class RefreshTokenEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private var id: Long,

        @OneToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        var token: String,

        @Column(nullable = false, unique = true)
        var expiryDate: LocalDate,

        @Column(nullable = false)
        var user: UserEntity
) : BaseEntity() {

}
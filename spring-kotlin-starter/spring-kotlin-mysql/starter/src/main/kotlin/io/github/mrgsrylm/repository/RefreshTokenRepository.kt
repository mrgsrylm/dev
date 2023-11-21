package io.github.mrgsrylm.repository

import io.github.mrgsrylm.entity.RefreshTokenEntity
import io.github.mrgsrylm.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByUserId(userId: Long): RefreshTokenEntity
    fun findByToken(token: String): RefreshTokenEntity?

    @Modifying
    fun deleteByUser(user: UserEntity): Int
}
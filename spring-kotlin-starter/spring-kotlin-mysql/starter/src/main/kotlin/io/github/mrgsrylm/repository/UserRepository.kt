package io.github.mrgsrylm.repository

import io.github.mrgsrylm.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
    fun findByUsername(username: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
}
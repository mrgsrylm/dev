package io.github.mrgsrylm.service

import io.github.mrgsrylm.entity.UserEntity

interface UserService {
    fun findById(id: Long): UserEntity?
    fun findByUsername(username: String): UserEntity?
    fun findByEmail(email: String): UserEntity?
}
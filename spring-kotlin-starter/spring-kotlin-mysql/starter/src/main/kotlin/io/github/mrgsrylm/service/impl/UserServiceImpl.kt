package io.github.mrgsrylm.service.impl

import io.github.mrgsrylm.entity.UserEntity
import io.github.mrgsrylm.repository.UserRepository
import io.github.mrgsrylm.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
        val repository: UserRepository
) : UserService{

    override fun findById(id: Long): UserEntity? {
        return repository.findById(id)
                .orElseThrow()
    }

    override fun findByUsername(username: String): UserEntity? {
        return repository.findByUsername(username)
    }

    override fun findByEmail(email: String): UserEntity? {
        TODO("Not yet implemented")
    }
}
package io.github.mrgsrylm.entity

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

@MappedSuperclass
abstract class BaseEntity {
    @Column(name = "CREATED_USER")
    lateinit var createdUser: String;

    @Column(name = "CREATED_AT")
    lateinit var createdAt: LocalDateTime;

    @Column(name = "UPDATED_USER")
    lateinit var updatedUser: String;

    @Column(name = "UPDATED_AT")
    lateinit var updatedAt: LocalDateTime;

    @PrePersist
    fun prePersist() {
        this.createdUser = getUsernameFromAuthentication()
        this.createdAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        this.updatedUser = getUsernameFromAuthentication()
        this.updatedAt = LocalDateTime.now()
    }

    private fun getUsernameFromAuthentication(): String {
        return "anonymousUser";
    }
}
package io.github.mrgsrylm.service.impl

import io.github.mrgsrylm.base.BaseServiceTest
import io.github.mrgsrylm.entity.UserEntity
import io.github.mrgsrylm.enums.Role
import io.github.mrgsrylm.repository.UserRepository
import io.github.mrgsrylm.utils.RandomUtil
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import java.util.*

class UserServiceImplTest : BaseServiceTest() {
    @InjectMocks
    private lateinit var service: UserServiceImpl

    @Mock
    private lateinit var repository: UserRepository

    @Test
    fun givenValidID_WhenFindByID_ReturnUser() {
        // Given
        val mockRec = UserEntity(
                id = 1L,
                fullName = RandomUtil.generateRandomString(),
                username = RandomUtil.generateRandomString(),
                email = RandomUtil.generateRandomString().plus("@kotlin.org"),
                password = RandomUtil.generateRandomString(),
                role = Role.ROLE_USER
        )

        // When
        Mockito.`when`(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockRec))

        // Then
        val result = service.findById(1L)

        Assertions.assertEquals(mockRec, result)
        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyLong())
    }

    @Test
    fun givenValidUsername_WhenFindByUsername_ReturnUser() {
        // Given
        val mockRec = UserEntity(
                id = 1L,
                fullName = RandomUtil.generateRandomString(),
                username = "johndoe",
                email = RandomUtil.generateRandomString().plus("@kotlin.org"),
                password = RandomUtil.generateRandomString(),
                role = Role.ROLE_USER
        )

        // When
        Mockito.`when`(repository.findByUsername(Mockito.anyString())).thenReturn(mockRec)

        // Then
        val result = service.findByUsername("johndoe")

        Assertions.assertEquals(mockRec, result)
        Mockito.verify(repository, Mockito.times(1)).findByUsername(Mockito.anyString())
    }

    @Test
    fun givenValidEmail_WhenFindByEmail_ReturnUser() {
        // Given
        val mockRec = UserEntity(
                id = 1L,
                fullName = RandomUtil.generateRandomString(),
                username = RandomUtil.generateRandomString(),
                email = "johndoe@kotlin.org",
                password = RandomUtil.generateRandomString(),
                role = Role.ROLE_USER
        )

        // When
        Mockito.`when`(repository.findByEmail(Mockito.anyString())).thenReturn(mockRec)

        // Then
        val result = service.findByEmail("johndoe")

        Assertions.assertEquals(mockRec, result)
        Mockito.verify(repository, Mockito.times(1)).findByEmail(Mockito.anyString())
    }
}
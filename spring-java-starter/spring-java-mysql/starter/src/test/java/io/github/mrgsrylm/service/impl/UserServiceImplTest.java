package io.github.mrgsrylm.service.impl;

import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import io.github.mrgsrylm.base.BaseServiceTest;
import io.github.mrgsrylm.util.RandomUtil;

import java.util.Optional;

public class UserServiceImplTest extends BaseServiceTest {
    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Test
    void givenValidId_WhenFindById_ReturnUser() {
        // Given
        UserEntity mockRec = UserEntity.builder()
                .id(1L)
                .fullName(RandomUtil.generateRandomString())
                .username(RandomUtil.generateRandomString())
                .email(RandomUtil.generateRandomString().concat("@wastecass.org"))
                .password(RandomUtil.generateRandomString())
                .build();

        Optional<UserEntity> mockOpt = Optional.ofNullable(mockRec);

        // When
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(mockRec));

        // Then
        Optional<UserEntity> result = service.findById(1L);

        Assertions.assertEquals(mockOpt, result);
        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void givenValidUsername_WhenFindByUsername_ReturnUser() {
        // Given
        UserEntity mockRec = UserEntity.builder()
                .id(1L)
                .fullName(RandomUtil.generateRandomString())
                .username(RandomUtil.generateRandomString())
                .email(RandomUtil.generateRandomString().concat("@wastecass.org"))
                .password(RandomUtil.generateRandomString())
                .build();

        Optional<UserEntity> mockOpt = Optional.ofNullable(mockRec);

        // When
        Mockito.when(repository.findByUsername(Mockito.anyString())).thenReturn(Optional.of(mockRec));

        // Then
        Optional<UserEntity> result = service.findByUsername(mockRec.getUsername());

        Assertions.assertEquals(mockOpt, result);
        Mockito.verify(repository, Mockito.times(1)).findByUsername(Mockito.anyString());
    }

    @Test
    void givenValidEmail_WhenFindByEmail_ReturnUser() {
        // Given
        UserEntity mockRec = UserEntity.builder()
                .id(1L)
                .fullName(RandomUtil.generateRandomString())
                .username(RandomUtil.generateRandomString())
                .email(RandomUtil.generateRandomString().concat("@wastecass.org"))
                .password(RandomUtil.generateRandomString())
                .build();

        Optional<UserEntity> mockOpt = Optional.ofNullable(mockRec);

        // When
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(mockRec));

        // Then
        Optional<UserEntity> result = service.findByEmail(mockRec.getEmail());

        Assertions.assertEquals(mockOpt, result);
        Mockito.verify(repository, Mockito.times(1)).findByEmail(Mockito.anyString());
    }
}

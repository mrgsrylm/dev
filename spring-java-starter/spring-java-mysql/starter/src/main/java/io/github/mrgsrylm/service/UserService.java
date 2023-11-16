package io.github.mrgsrylm.service;

import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.payload.user.UserInfoResponse;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    UserInfoResponse getUserInfo(Long id);
}

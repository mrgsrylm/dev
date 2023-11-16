package io.github.mrgsrylm.service;

import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {
    String createRefreshToken(UserEntity user);
    boolean isRefreshExpired(RefreshTokenEntity token);
    RefreshTokenEntity getByUser(Long userId);
    Optional<RefreshTokenEntity> findByToken(String token);
    int deleteByUserId(Long userId);
}

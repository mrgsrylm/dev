package io.github.mrgsrylm.repository;

import io.github.mrgsrylm.entity.RefreshTokenEntity;
import io.github.mrgsrylm.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    RefreshTokenEntity findByUserId(Long userId);
    Optional<RefreshTokenEntity> findByToken(String token);
    @Modifying
    int deleteByUser(UserEntity user);
}
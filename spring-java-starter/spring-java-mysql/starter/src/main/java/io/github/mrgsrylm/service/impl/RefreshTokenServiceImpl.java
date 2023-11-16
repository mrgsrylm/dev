package io.github.mrgsrylm.service.impl;

import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.exception.custom.UserNotFoundException;
import io.github.mrgsrylm.service.RefreshTokenService;
import io.github.mrgsrylm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.github.mrgsrylm.entity.RefreshTokenEntity;
import io.github.mrgsrylm.repository.RefreshTokenRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository repository;
    private final UserService userService;

    @Value("${jwt.refreshExpireMs}")
    Long expireSeconds;

    @Override
    public String createRefreshToken(UserEntity user) {
        RefreshTokenEntity token = getByUser(user.getId());
        if (token == null) {
            token = new RefreshTokenEntity();
            token.setUser(user);
        }

        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusSeconds(expireSeconds).atZone(ZoneOffset.UTC).toLocalDate());
        repository.save(token);

        return token.getToken();
    }

    @Override
    public boolean isRefreshExpired(RefreshTokenEntity token) {
        return token.getExpiryDate().isBefore(LocalDate.now());
    }

    @Override
    public RefreshTokenEntity getByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    @Transactional
    public int deleteByUserId(Long userId) {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return repository.deleteByUser(user);
    }
}

package io.github.mrgsrylm.builder;

import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.entity.RefreshTokenEntity;
import io.github.mrgsrylm.util.RandomUtil;

import java.time.LocalDate;

public class RefreshTokenBuilder extends BaseBuilder<RefreshTokenEntity> {
    public RefreshTokenBuilder() {
        super(RefreshTokenEntity.class);
    }

    public RefreshTokenBuilder refreshToken() {
        return this
                .withId(1L)
                .withToken(RandomUtil.generateRandomString())
                .withExpiryDate(LocalDate.now().plusDays(1));

    }

    public RefreshTokenBuilder withId(Long id) {
        data.setId(id);
        return this;
    }

    public RefreshTokenBuilder withToken(String token) {
        data.setToken(token);
        return this;
    }

    public RefreshTokenBuilder withExpiryDate(LocalDate expiry) {
        data.setExpiryDate(expiry);
        return this;
    }

    public RefreshTokenBuilder withUser(UserEntity user) {
        data.setUser(user);
        return this;
    }
}

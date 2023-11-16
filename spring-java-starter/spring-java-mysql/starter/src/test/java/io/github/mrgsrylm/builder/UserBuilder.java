package io.github.mrgsrylm.builder;

import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.enums.Role;
import io.github.mrgsrylm.util.RandomUtil;

public class UserBuilder extends BaseBuilder<UserEntity> {
    public UserBuilder() {
        super(UserEntity.class);
    }

    public UserBuilder user() {
        return this
                .withId(1L)
                .withFullName(RandomUtil.generateRandomString())
                .withUsername(RandomUtil.generateRandomString())
                .withEmail(RandomUtil.generateRandomString().concat("@example.com"))
                .withRole(Role.ROLE_USER);
    }

    public UserBuilder admin() {
        return this.user()
                .withRole(Role.ROLE_ADMIN);
    }

    public UserBuilder withId(Long id) {
        data.setId(id);
        return this;
    }

    public UserBuilder withFullName(String fullName) {
        data.setFullName(fullName);
        return this;
    }

    public UserBuilder withUsername(String username) {
        data.setUsername(username);
        return this;
    }

    public UserBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public UserBuilder withRole(Role role) {
        data.setRole(role);
        return this;
    }
}

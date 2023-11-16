package io.github.mrgsrylm.service.impl;

import io.github.mrgsrylm.exception.custom.UserNotFoundException;
import io.github.mrgsrylm.payload.user.UserInfoResponse;
import io.github.mrgsrylm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Optional<UserEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public UserInfoResponse getUserInfo(Long id) {
        UserEntity rec = repository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return UserInfoResponse.builder()
                .id(rec.getId())
                .fullName(rec.getFullName())
                .username(rec.getUsername())
                .email(rec.getEmail())
                .role(rec.getRole())
                .build();
    }
}

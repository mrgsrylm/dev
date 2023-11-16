package io.github.mrgsrylm.payload.user;

import io.github.mrgsrylm.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class UserInfoResponse {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private Role role;
}


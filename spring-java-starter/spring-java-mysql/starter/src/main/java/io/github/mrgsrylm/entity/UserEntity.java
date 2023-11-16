package io.github.mrgsrylm.entity;

import io.github.mrgsrylm.enums.TokenClaims;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.github.mrgsrylm.enums.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    public Map<String, Object> getClaims() {
        final Map<String, Object> claims = new HashMap<>();
        claims.put(TokenClaims.ID.getValue(), this.id);
        claims.put(TokenClaims.USERNAME.getValue(), this.username);
        claims.put(TokenClaims.ROLES.getValue(), List.of(this.role));
        claims.put(TokenClaims.USER_FULL_NAME.getValue(), this.fullName);
        claims.put(TokenClaims.EMAIL.getValue(), this.email);
        return claims;
    }
}

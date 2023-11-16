package io.github.mrgsrylm.payload.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private String token;

    @Builder.Default
    private String type = "Bearer";
    private String refreshToken;
    private String email;
}

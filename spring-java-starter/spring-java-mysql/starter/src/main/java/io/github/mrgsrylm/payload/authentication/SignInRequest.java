package io.github.mrgsrylm.payload.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInRequest {
    @NotBlank
    @Size(min = 5, max = 50)
    @Email
    private String email;

    @NotBlank
    private String password;
}

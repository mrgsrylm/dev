package io.github.mrgsrylm.springbootjavamonolithboilerplate.dto.request.auth;

import io.github.mrgsrylm.springbootjavamonolithboilerplate.dto.request.user.AbstractBaseCreateUserRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class RegisterRequest extends AbstractBaseCreateUserRequest {
}

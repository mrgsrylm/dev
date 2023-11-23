package io.github.mrgsrylm.springbootjavamonolithboilerplate.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class UpdateProfileRequest extends AbstractBaseUpdateUserRequest {
}

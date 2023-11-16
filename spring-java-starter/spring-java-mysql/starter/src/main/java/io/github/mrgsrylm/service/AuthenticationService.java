package io.github.mrgsrylm.service;

import io.github.mrgsrylm.payload.authentication.*;
import org.superosystem.wasteclass.payload.authentication.*;

public interface AuthenticationService {
    String register(SignUpRequest request);
    JwtResponse login(SignInRequest request);
    TokenRefreshResponse refreshToken(TokenRefreshRequest request);
    String logout(String token);
}

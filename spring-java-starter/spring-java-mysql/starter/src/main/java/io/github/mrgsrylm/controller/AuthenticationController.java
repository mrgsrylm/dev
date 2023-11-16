package io.github.mrgsrylm.controller;

import io.github.mrgsrylm.payload.authentication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.github.mrgsrylm.payload.CustomResponse;
import org.superosystem.wasteclass.payload.authentication.*;
import io.github.mrgsrylm.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomResponse<String> register(@RequestBody SignUpRequest request) {
        return CustomResponse.created(service.register(request));
    }

    @PostMapping("/login")
    public CustomResponse<JwtResponse> login(@RequestBody SignInRequest request) {
        return CustomResponse.ok(service.login(request));
    }

    @PostMapping("/refreshToken")
    public CustomResponse<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        return CustomResponse.ok(service.refreshToken(request));
    }

    @PostMapping("/logout")
    public CustomResponse<String> logout(@RequestHeader("Authorization") String token) {
        return CustomResponse.ok(service.logout(token));
    }
}

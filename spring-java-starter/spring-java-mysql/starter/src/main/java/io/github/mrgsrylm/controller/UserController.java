package io.github.mrgsrylm.controller;

import io.github.mrgsrylm.payload.CustomResponse;
import io.github.mrgsrylm.payload.user.UserInfoResponse;
import io.github.mrgsrylm.security.CustomUserDetails;
import io.github.mrgsrylm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @GetMapping("/info")
    @ResponseStatus(HttpStatus.OK)
    public CustomResponse<UserInfoResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails user) {
        return CustomResponse.ok(service.getUserInfo(user.getId()));
    }
}

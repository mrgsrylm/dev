package io.github.mrgsrylm.service.impl;

import io.github.mrgsrylm.payload.authentication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import io.github.mrgsrylm.entity.RefreshTokenEntity;
import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.enums.Role;
import io.github.mrgsrylm.exception.custom.EmailAlreadyExistsException;
import io.github.mrgsrylm.exception.custom.RefreshTokenNotFoundException;
import io.github.mrgsrylm.exception.custom.UserNotFoundException;
import io.github.mrgsrylm.exception.custom.UsernameAlreadyExistsException;
import org.superosystem.wasteclass.payload.authentication.*;
import io.github.mrgsrylm.repository.UserRepository;
import io.github.mrgsrylm.security.CustomUserDetails;
import io.github.mrgsrylm.security.jwt.JwtUtils;
import io.github.mrgsrylm.service.AuthenticationService;
import io.github.mrgsrylm.service.RefreshTokenService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    @Override
    public String register(SignUpRequest request) {
        if  (repository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyExistsException(request.getEmail());

        if  (repository.existsByUsername(request.getUsername()))
            throw new UsernameAlreadyExistsException(request.getUsername());

        if (request.getRole() == null)
            request.setRole(Role.ROLE_USER);

        UserEntity rec = UserEntity.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        repository.save(rec);

        return "success";
    }

    @Override
    public JwtResponse login(SignInRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        Authentication auth = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwtToken = jwtUtils.generateJwtToken(auth);

        UserEntity rec = repository.findByEmail(request.getEmail())
                .orElseThrow(UserNotFoundException::new);

        return JwtResponse.builder()
                .email(rec.getEmail())
                .token(jwtToken)
                .refreshToken(refreshTokenService.createRefreshToken(rec))
                .build();
    }

    @Override
    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        RefreshTokenEntity rts = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(RefreshTokenNotFoundException::new);

        if (!refreshTokenService.isRefreshExpired(rts)) {
            CustomUserDetails customUserDetails = new CustomUserDetails(rts.getUser());
            String newToken = jwtUtils.generateJwtToken(customUserDetails);

            return TokenRefreshResponse.builder()
                    .accessToken(newToken)
                    .refreshToken(rts.getToken())
                    .build();
        }

        return null;
    }

    @Override
    public String logout(String token) {
        String authToken = jwtUtils.extractTokenFromHeader(token);

        if (jwtUtils.validateJwtToken(authToken)) {
            Long id = jwtUtils.getIdFromToken(authToken);
            refreshTokenService.deleteByUserId(id);

            return "success";
        }

        return "failed";
    }
}

package io.github.mrgsrylm.service.impl;

import io.github.mrgsrylm.entity.RefreshTokenEntity;
import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.payload.authentication.*;
import io.github.mrgsrylm.repository.UserRepository;
import io.github.mrgsrylm.security.jwt.JwtUtils;
import io.github.mrgsrylm.service.RefreshTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import io.github.mrgsrylm.base.BaseServiceTest;
import io.github.mrgsrylm.enums.Role;
import org.superosystem.wasteclass.payload.authentication.*;
import io.github.mrgsrylm.security.CustomUserDetails;

import java.util.Optional;

public class AuthenticationServiceImplTest extends BaseServiceTest {
    @InjectMocks
    private AuthenticationServiceImpl service;

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private RefreshTokenService refreshTokenService;

    @Test
    void givenSignUpRequest_WhenRegister_ReturnSuccess() {
        // Given
        SignUpRequest request = SignUpRequest.builder()
                .fullName("john_doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("my-secret-pw")
                .role(Role.ROLE_USER)
                .build();

        UserEntity rec = UserEntity.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // When
        Mockito.when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(rec);

        // Then
        String result = service.register(request);

        Assertions.assertEquals("success", result);
        Mockito.verify(userRepository).save(Mockito.any(UserEntity.class));
    }

    @Test
    void givenAlreadyUsername_WhenRegister_ReturnException() {
        // Given
        SignUpRequest request = SignUpRequest.builder()
                .fullName("john_doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("my-secret-pw")
                .role(Role.ROLE_USER)
                .build();

        UserEntity rec = UserEntity.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // When
        Mockito.when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        // Then
        Assertions.assertThrows(Exception.class, () -> service.register(request));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
    }

    @Test
    void givenAlreadyEmail_WhenRegister_ReturnException() {
        // Given
        SignUpRequest request = SignUpRequest.builder()
                .fullName("john_doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("my-secret-pw")
                .role(Role.ROLE_USER)
                .build();

        UserEntity rec = UserEntity.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // When
        Mockito.when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Then
        Assertions.assertThrows(Exception.class, () -> service.register(request));
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
    }

    @Test
    void givenSignInRequest_WhenLogin_ReturnJwtResponse() {
        // Given
        SignInRequest request = SignInRequest.builder()
                .email("johndoe@example.com")
                .password("my-secret-pw")
                .build();

        UserEntity mockRec = UserEntity.builder()
                .fullName("John Doe")
                .username("johndoe")
                .email(request.getEmail())
                .password("hashed_password")
                .role(Role.ROLE_USER)
                .build();

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());

        // When
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        Mockito.when(jwtUtils.generateJwtToken(mockAuth)).thenReturn("mockedToken");
        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(mockRec));
        Mockito.when(refreshTokenService.createRefreshToken(Mockito.any(UserEntity.class)))
                .thenReturn("actualRefreshToken");

        // Then
        JwtResponse result = service.login(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(request.getEmail(), result.getEmail());
        Assertions.assertEquals("mockedToken", result.getToken());
        Assertions.assertEquals("actualRefreshToken", result.getRefreshToken());
    }

    @Test
    void givenSignInRequest_WhenLogin_ReturnException() {
        // Given
        SignInRequest request = SignInRequest.builder()
                .email("johndoe@example.com")
                .password("my-secret-pw")
                .build();

        // When
        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);

        // Then

        Assertions.assertThrows(RuntimeException.class, () -> service.login(request));
    }

    @Test
    void givenTokenRefreshRequest_WhenRefreshToken_ReturnSuccess() {
        // Given
        TokenRefreshRequest request = TokenRefreshRequest.builder()
                .refreshToken("validRefreshToken")
                .build();

        RefreshTokenEntity mockRec = RefreshTokenEntity.builder()
                .token("validRefreshToken")
                .user(UserEntity.builder().id(1L).build())
                .build();

        // When
        Mockito.when(refreshTokenService.findByToken(request.getRefreshToken()))
                .thenReturn(Optional.of(mockRec));
        Mockito.when(refreshTokenService.isRefreshExpired(mockRec))
                .thenReturn(false);
        Mockito.when(jwtUtils.generateJwtToken(Mockito.any(CustomUserDetails.class)))
                .thenReturn("newMockedToken");

        // Then
        TokenRefreshResponse result = service.refreshToken(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("newMockedToken", result.getAccessToken());
        Assertions.assertEquals("validRefreshToken", result.getRefreshToken());
    }

    @Test
    void givenTokenRefreshRequest_WhenRefreshToken_ReturnFailed() {
        // Given
        TokenRefreshRequest request = TokenRefreshRequest.builder()
                .refreshToken("invalidRefreshToken")
                .build();

        RefreshTokenEntity mockRec = RefreshTokenEntity.builder()
                .token("validRefreshToken")
                .user(UserEntity.builder().id(1L).build())
                .build();

        // When
        Mockito.when(refreshTokenService.findByToken(request.getRefreshToken()))
                .thenReturn(Optional.empty());

        // Then
        Assertions.assertThrows(Exception.class, () -> service.refreshToken(request));
    }

    @Test
    void givenAccessToken_WhenLogout_ReturnSuccess() {
        // Given
        String token = "validAuthToken";
        Long userId = 1L;

        // When
        Mockito.when(jwtUtils.extractTokenFromHeader(token)).thenReturn(token);
        Mockito.when(jwtUtils.validateJwtToken(token)).thenReturn(true);
        Mockito.when(jwtUtils.getIdFromToken(token)).thenReturn(userId);

        // Then
        String result = service.logout(token);

        Assertions.assertEquals("success", result);
        Mockito.verify(refreshTokenService).deleteByUserId(userId);
    }

    @Test
    void givenInvalidAccessToken_WhenCustomerRole_ReturnLogoutFailed() {
        // Given
        String token = "invalidAuthToken";

        Mockito.when(jwtUtils.extractTokenFromHeader(token)).thenReturn(null); // Invalid token

        // When
        String result = service.logout(token);

        // Then
        Assertions.assertEquals("failed", result);
        Mockito.verify(refreshTokenService, Mockito.never()).deleteByUserId(Mockito.anyLong());
    }

    @Test
    void givenInvalidAccessToken_WhenCustomerRole_ReturnLogoutInvalidJwtToken() {
        // Given
        String token = "invalidAuthToken";

        Mockito.when(jwtUtils.extractTokenFromHeader(token)).thenReturn(token);
        Mockito.when(jwtUtils.validateJwtToken(token)).thenReturn(false);

        // When
        String result = service.logout(token);

        // Then
        Assertions.assertEquals("failed", result);
        Mockito.verify(refreshTokenService, Mockito.never()).deleteByUserId(Mockito.anyLong());

    }
}

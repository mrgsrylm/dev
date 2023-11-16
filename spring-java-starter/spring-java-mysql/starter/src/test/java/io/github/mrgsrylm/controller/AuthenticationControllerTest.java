package io.github.mrgsrylm.controller;

import io.github.mrgsrylm.base.BaseControllerTest;
import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.payload.authentication.*;
import io.github.mrgsrylm.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import io.github.mrgsrylm.enums.Role;
import org.superosystem.wasteclass.payload.authentication.*;
import io.github.mrgsrylm.security.CustomUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest extends BaseControllerTest {
    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @Test
    void givenSignUpRequest_WhenRegister_ReturnSuccess() throws Exception {
        // Given
        SignUpRequest request = SignUpRequest.builder()
                .fullName("John Doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .password("my-secret-pw")
                .role(Role.ROLE_USER)
                .build();

        // When
        Mockito.when(authenticationService.register(request)).thenReturn("success");

        // Then
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void givenLoginRequest_WhenLogin_ReturnSuccess() throws Exception {
        // Given
        SignInRequest request = SignInRequest.builder()
                .email("johndoe@example.com")
                .password("my-secret-pw")
                .build();

        JwtResponse mockResponse = JwtResponse.builder()
                .email(request.getEmail())
                .token("mockedToken")
                .refreshToken("mockedRefreshToken")
                .build();

        // When
        Mockito.when(authenticationService.login(request)).thenReturn(mockResponse);

        // Then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void givenRefreshTokenRequestAndAccessToken_WhenRefreshToken_ReturnRefreshTokenSuccess() throws Exception {
        // Given
        UserEntity mockUser = UserEntity.builder()
                .id(1L)
                .fullName("John Doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .role(Role.ROLE_USER)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);
        String accessToken = jwtUtils.generateJwtToken(userDetails);
        String mockBearerToken = "Bearer " + accessToken;
        TokenRefreshRequest request = TokenRefreshRequest.builder()
                .refreshToken("validRefreshToken")
                .build();

        TokenRefreshResponse mockResponse = TokenRefreshResponse.builder()
                .accessToken("newMockedToken")
                .refreshToken("validRefreshToken")
                .build();

        // when
        Mockito.when(authenticationService.refreshToken(request)).thenReturn(mockResponse);
        Mockito.when(customUserDetailsService.loadUserByUsername("johndoe@example.com"))
                .thenReturn(userDetails);

        // then
        mockMvc.perform(post("/api/v1/auth/refreshToken")
                        .header(HttpHeaders.AUTHORIZATION, mockBearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void givenAccessToken_WhenCustomerRole_ReturnLogoutSuccess() throws Exception {
        // Given
        UserEntity mockUser = UserEntity.builder()
                .id(1L)
                .fullName("John Doe")
                .username("johndoe")
                .email("johndoe@example.com")
                .role(Role.ROLE_USER)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(mockUser);
        String accessToken = jwtUtils.generateJwtToken(userDetails);
        String mockBearerToken = "Bearer " + accessToken;

        // When
        Mockito.when(customUserDetailsService.loadUserByUsername("johndoe@example.com"))
                .thenReturn(userDetails);
        Mockito.when(authenticationService.logout(mockBearerToken)).thenReturn("success");

        // Then
        mockMvc.perform(post("/api/v1/auth/logout")
                        .header(HttpHeaders.AUTHORIZATION, mockBearerToken))
                .andExpect(status().isOk());

        Mockito.verify(authenticationService).logout(mockBearerToken);

    }
}

package io.github.mrgsrylm.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.mrgsrylm.builder.UserBuilder;
import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.logging.entity.LogEntity;
import io.github.mrgsrylm.logging.service.impl.LogServiceImpl;
import io.github.mrgsrylm.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import io.github.mrgsrylm.security.CustomUserDetails;
import io.github.mrgsrylm.security.CustomUserDetailsService;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseControllerTest extends BaseTestContainer {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected CustomUserDetailsService customUserDetailsService;

    @MockBean
    protected LogServiceImpl logService;

    @Autowired
    protected JwtUtils jwtUtils;

    protected UserEntity mockUser;

    protected String mockUserToken;

    @BeforeEach
    protected void initializeAuth() {
        this. mockUser = new UserBuilder().user().build();

        final CustomUserDetails mockUserDetails = new CustomUserDetails(mockUser);

        this.mockUserToken = generateMockToken(mockUserDetails);

        Mockito.when(customUserDetailsService.loadUserByUsername(mockUser.getEmail()))
                .thenReturn(mockUserDetails);
        Mockito.doNothing().when(logService).saveLogToDatabase(Mockito.any(LogEntity.class));
    }

    private String generateMockToken(CustomUserDetails details) {
        return "Bearer ".concat(jwtUtils.generateJwtToken(details));
    }
}

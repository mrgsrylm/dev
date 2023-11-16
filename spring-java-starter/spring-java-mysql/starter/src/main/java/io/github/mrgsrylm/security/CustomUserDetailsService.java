package io.github.mrgsrylm.security;

import io.github.mrgsrylm.entity.UserEntity;
import io.github.mrgsrylm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService service;

    /**
     * Load user details by username.
     *
     * @param email The email (in this case, the user's email address).
     * @return A UserDetails object representing the user, or throw a UsernameNotFoundException if the user is not found.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity rec = null;
        try {
            rec = service.findByEmail(email)
                    .orElseThrow(() -> new Exception("user " + email + " is not found"));
        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return new CustomUserDetails(rec);
    }
}

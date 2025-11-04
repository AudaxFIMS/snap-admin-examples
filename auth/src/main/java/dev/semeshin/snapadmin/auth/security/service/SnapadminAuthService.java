package dev.semeshin.snapadmin.auth.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "snapadmin.enabled", havingValue = "true")
public class SnapadminAuthService {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();


    public SnapadminAuthService(
            UserDetailsService userDetailsService
    ) {
        this.userDetailsService = userDetailsService;
    }

    public Map<String, Object> authenticate(String username, String password) throws JsonProcessingException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails == null
                || !userDetails.isAccountNonExpired()
                || !userDetails.isAccountNonLocked()
                || !userDetails.isCredentialsNonExpired()
                || !userDetails.isEnabled()
        ) {
            throw new RuntimeException("Invalid credentials or account is disabled by UserDetails service");
        }

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            HashMap<String, Object> authData = new HashMap<>();

            authData.put("user_name", userDetails.getUsername());
            authData.put("user_data", userDetails.getUsername());
            authData.put("user_roles", userDetails.getAuthorities());

            return authData;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }
}
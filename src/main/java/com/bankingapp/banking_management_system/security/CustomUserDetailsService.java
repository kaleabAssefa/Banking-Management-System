package com.bankingapp.banking_management_system.security;

import com.bankingapp.banking_management_system.entity.User;
import com.bankingapp.banking_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Lombok generates a constructor for all 'final' fields = constructor injection
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Spring Security's UserDetails expects roles prefixed with "ROLE_"
        // e.g. RoleName.ADMIN becomes authority "ROLE_ADMIN" — this is what
        // @PreAuthorize("hasRole('ADMIN')") checks against later.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())))
                .disabled(!user.isEnabled())
                .build();
    }
}

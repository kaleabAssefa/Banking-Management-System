package com.bankingapp.banking_management_system.service;
import com.bankingapp.banking_management_system.dto.*;
import com.bankingapp.banking_management_system.entity.*;
import com.bankingapp.banking_management_system.enums.RoleName;
import com.bankingapp.banking_management_system.repository.*;
import com.bankingapp.banking_management_system.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // New public registrations are always CUSTOMER — employees/admins
        // are created separately by an admin later (Milestone 4), never
        // through this open self-registration endpoint.
        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER)
                .orElseThrow(() -> new IllegalStateException("CUSTOMER role not seeded"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // NEVER store raw password
                .role(customerRole)
                .enabled(true)
                .build();
        user = userRepository.save(user);

        Customer customer = Customer.builder()
                .user(user)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        customerRepository.save(customer);

        String token = jwtService.generateToken(user.getEmail(), customerRole.getName().name());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(customerRole.getName().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // This throws an exception automatically if credentials are wrong —
        // Spring Security compares the raw password against the BCrypt hash
        // internally, we never do that comparison ourselves.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("User not found after authentication"));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().getName().name());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .build();
    }
}
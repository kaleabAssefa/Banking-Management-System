package com.bankingapp.banking_management_system.dto;

import lombok.*;

// What we send BACK after successful login/registration.
// Notice: no password field here, ever.
@Getter @Setter
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private String role;
}
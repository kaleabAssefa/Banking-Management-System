package com.bankingapp.banking_management_system.dto;

import jakarta.validation.constraints.*;
import lombok.*;

// This is what the CLIENT sends us to register — notice it's completely
// separate from the User entity. We control exactly what fields are
// accepted here, and validation happens automatically via these annotations.
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
}
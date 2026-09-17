package com.bankingapp.banking_management_system.entity;

import com.bankingapp.banking_management_system.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter @Setter               // Lombok generates all getters/setters at compile time
@NoArgsConstructor            // JPA requires a no-args constructor to build objects via reflection
@AllArgsConstructor
@Builder                      // Lets us do Role.builder().name(...).build() — useful in tests/seed data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-increments the id
    private Long id;

    @Enumerated(EnumType.STRING) // Stores "CUSTOMER" (readable) instead of 0,1,2 (ordinal — fragile if enum order changes)
    @Column(unique = true, nullable = false, length = 20)
    private RoleName name;
}
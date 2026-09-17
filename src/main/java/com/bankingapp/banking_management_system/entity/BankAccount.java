package com.bankingapp.banking_management_system.entity;

import com.bankingapp.banking_management_system.enums.AccountStatus;
import com.bankingapp.banking_management_system.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_accounts")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many BankAccounts belong to one Customer — a customer can have
    // both a checking AND a savings account, for example.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(unique = true, nullable = false, length = 20)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;

    // precision = total digits, scale = digits after the decimal point.
    // 19,2 comfortably covers real-world account balances.
    @Column(nullable = false, precision = 19, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
package com.bankingapp.banking_management_system.repository;

import com.bankingapp.banking_management_system.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

// Not used yet - included now so it's ready for Milestone 5 (Account Management).
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}

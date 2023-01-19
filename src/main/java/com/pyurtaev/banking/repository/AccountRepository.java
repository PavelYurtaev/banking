package com.pyurtaev.banking.repository;

import com.pyurtaev.banking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}

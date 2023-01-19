package com.pyurtaev.banking.service;

import com.pyurtaev.banking.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserService {
    Long createUserEmail(Long userId, String email);

    Long createUserPhone(Long userId, String phone);

    void updateUserEmail(Long emailDataId, String email);

    void updateUserPhone(Long phoneDataId, String phone);

    Page<User> searchUsers(Pageable pageable, final LocalDate dateOfBirth, final String name, final String phone,
            final String email);

    String getUserToken(String email, String password);

    User getById(Long id);

    void transferMoney(Long userIdTransferFrom, Long userIdTransferTo, BigDecimal value);

    void addPercentsToAccounts();

}

package com.pyurtaev.banking.service.impl;

import com.pyurtaev.banking.config.JwtService;
import com.pyurtaev.banking.exception.BankingException;
import com.pyurtaev.banking.model.EmailData;
import com.pyurtaev.banking.model.User;
import com.pyurtaev.banking.repository.UserRepository;
import com.pyurtaev.banking.service.EmailDataService;
import com.pyurtaev.banking.service.PhoneDataService;
import com.pyurtaev.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final BigDecimal BIG_DECIMAL_HUNDRED = BigDecimal.valueOf(100);

    private final UserRepository userRepository;
    private final EmailDataService emailDataService;
    private final PhoneDataService phoneDataService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;


    @Override
    public Long createUserEmail(final Long userId, final String email) {
        return emailDataService.createEmailData(new User(userId), email);
    }

    @Override
    public Long createUserPhone(final Long userId, final String phone) {
        return phoneDataService.createPhoneData(new User(userId), phone);
    }

    @Override
    public void updateUserEmail(final Long emailDataId, final String email) {
        emailDataService.updateEmailData(emailDataId, email);
    }

    @Override
    public void updateUserPhone(final Long phoneDataId, final String phone) {
        phoneDataService.updatePhoneData(phoneDataId, phone);
    }

    @Override
    public Page<User> searchUsers(final Pageable pageable, final LocalDate dateOfBirth, final String name,
            final String phone, final String email) {

        final List<Specification<User>> specs = new ArrayList<>();
        if (phone != null && !phone.isBlank()) {
            specs.add((user, query, builder) -> builder.equal(user.join("phoneData").get("phone"), phone));
        }
        if (email != null && !email.isBlank()) {
            specs.add((user, query, builder) -> builder.equal(user.join("emailData").get("email"), email));
        }
        if (dateOfBirth != null) {
            specs.add((user, query, builder) -> builder.greaterThanOrEqualTo(user.get("dateOfBirth"), dateOfBirth));
        }
        if (name != null && !name.isBlank()) {
            specs.add((user, query, builder) -> builder.like(user.get("name"), name + "%"));
        }

        return specs.isEmpty()
                ? userRepository.findAll(pageable)
                : userRepository.findAll(specs.stream().reduce(Specification::and).get(), pageable);
    }

    @Override
    public String getUserToken(final String email, final String password) {
        final EmailData emailData = emailDataService.getByEmail(email);
        final User user = emailData.getUser();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtService.generateToken(user.getId());
        } else {
            throw new BankingException("Failed to auth");
        }
    }

    @Override
    public User getById(final Long id) {
        return userRepository.findById(id).orElseThrow(() -> new BankingException("User not found by id " + id));
    }

    @Override
    @Transactional
    public synchronized void transferMoney(final Long userIdTransferFrom, final Long userIdTransferTo,
            final BigDecimal value) {
        final User userFrom = getById(userIdTransferFrom);
        final BigDecimal resultBalance = userFrom.getAccount().getBalance().subtract(value);
        if (resultBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BankingException("Not enough money to transfer");
        } else {
            userFrom.getAccount().setBalance(resultBalance);
        }
        final User userTo = getById(userIdTransferTo);
        userTo.getAccount().setBalance(userTo.getAccount().getBalance().add(value));

        userRepository.saveAll(List.of(userTo, userFrom));
    }

    @Override
    @Transactional
    public synchronized void addPercentsToAccounts() {
        final List<User> users = userRepository.findAllByIdIn(AccumulationScheduler.balanceAutoIncreaseLimits.keySet());

        for (User user : users) {
            final BigDecimal currentBalance = user.getAccount().getBalance();
            final BigDecimal resultValue = currentBalance.add(
                    currentBalance.multiply(BigDecimal.TEN).divide(BIG_DECIMAL_HUNDRED, 2, RoundingMode.HALF_UP));
            final BigDecimal limit = AccumulationScheduler.balanceAutoIncreaseLimits.get(user.getId());
            if (resultValue.compareTo(limit) > 0) {
                AccumulationScheduler.balanceAutoIncreaseLimits.remove(user.getId());
            } else {
                user.getAccount().setBalance(resultValue);
                userRepository.save(user);
            }
        }

    }

    @Override
    public UserDetails loadUserByUsername(final String userId) throws UsernameNotFoundException {
        final User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new BankingException("User not found"));
        return new org.springframework.security.core.userdetails.User(String.valueOf(user.getId()), "", List.of());

    }
}

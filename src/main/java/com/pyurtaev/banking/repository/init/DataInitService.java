package com.pyurtaev.banking.repository.init;

import com.pyurtaev.banking.model.Account;
import com.pyurtaev.banking.model.EmailData;
import com.pyurtaev.banking.model.PhoneData;
import com.pyurtaev.banking.model.User;
import com.pyurtaev.banking.repository.AccountRepository;
import com.pyurtaev.banking.repository.EmailDataRepository;
import com.pyurtaev.banking.repository.PhoneDataRepository;
import com.pyurtaev.banking.repository.UserRepository;
import com.pyurtaev.banking.service.impl.AccumulationScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DataInitService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PhoneDataRepository phoneDataRepository;
    private final EmailDataRepository emailDataRepository;
    private final AccumulationScheduler accumulationScheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void initUsers() {
        userRepository.deleteAll();
        User user = userRepository.save(User.builder()
                .name("Jack")
                .dateOfBirth(LocalDate.of(1993, 5, 1))
                .password(passwordEncoder.encode("test"))
                .build());
        final BigDecimal balance1 = BigDecimal.valueOf(100);
        accountRepository.save(Account.builder().balance(balance1).user(user).build());
        phoneDataRepository.save(PhoneData.builder().user(user).phone("79998887766").build());
        emailDataRepository.save(EmailData.builder().user(user).email("mail@mail.mail").build());
        accumulationScheduler.registerUserLimit(user.getId(), balance1);

        user = userRepository.save(User.builder()
                .name("Peter")
                .dateOfBirth(LocalDate.of(1985, 4, 4))
                .password(passwordEncoder.encode("test2"))
                .build());
        final BigDecimal balance2 = BigDecimal.valueOf(200);
        accountRepository.save(Account.builder().balance(balance2).user(user).build());
        phoneDataRepository.save(PhoneData.builder().user(user).phone("79991112233").build());
        emailDataRepository.save(EmailData.builder().user(user).email("post@post.post").build());
        accumulationScheduler.registerUserLimit(user.getId(), balance2);
    }
}

package com.pyurtaev.banking.service.impl;

import com.pyurtaev.banking.exception.BankingException;
import com.pyurtaev.banking.model.EmailData;
import com.pyurtaev.banking.model.User;
import com.pyurtaev.banking.repository.EmailDataRepository;
import com.pyurtaev.banking.service.EmailDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailDataServiceImpl implements EmailDataService {

    private final EmailDataRepository emailDataRepository;

    @Override
    public EmailData getById(final Long id) {
        return emailDataRepository.findById(id)
                .orElseThrow(() -> new BankingException("Not found EmailData by id " + id));
    }

    @Override
    public EmailData getByEmail(final String email) {
        return emailDataRepository.findByEmail(email)
                .orElseThrow(() -> new BankingException("Not found EmailData by email " + email));
    }

    @Override
    public Long createEmailData(final User user, final String email) {
        final EmailData emailData = EmailData.builder()
                .user(user)
                .email(email)
                .build();
        return emailDataRepository.save(emailData).getId();
    }

    @Override
    public void updateEmailData(final Long emailDataId, final String email) {
        final EmailData emailData = getById(emailDataId);
        emailData.setEmail(email);
        emailDataRepository.save(emailData);
    }
}

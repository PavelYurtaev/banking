package com.pyurtaev.banking.service.impl;

import com.pyurtaev.banking.exception.BankingException;
import com.pyurtaev.banking.model.PhoneData;
import com.pyurtaev.banking.model.User;
import com.pyurtaev.banking.repository.PhoneDataRepository;
import com.pyurtaev.banking.service.PhoneDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhoneDataServiceImpl implements PhoneDataService {

    private final PhoneDataRepository phoneDataRepository;

    @Override
    public PhoneData getById(final Long id) {
        return phoneDataRepository.findById(id)
                .orElseThrow(() -> new BankingException("Not found PhoneData by id " + id));
    }

    @Override
    public Long createPhoneData(final User user, final String phone) {
        final PhoneData phoneData = PhoneData.builder()
                .user(user)
                .phone(phone)
                .build();
        return phoneDataRepository.save(phoneData).getId();
    }

    @Override
    public void updatePhoneData(final Long phoneDataId, final String phone) {
        final PhoneData phoneData = getById(phoneDataId);
        phoneData.setPhone(phone);
        phoneDataRepository.save(phoneData);
    }
}

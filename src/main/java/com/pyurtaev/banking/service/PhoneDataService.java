package com.pyurtaev.banking.service;

import com.pyurtaev.banking.model.PhoneData;
import com.pyurtaev.banking.model.User;

public interface PhoneDataService {

    PhoneData getById(Long id);

    Long createPhoneData(User user, String phone);

    void updatePhoneData(Long phoneDataId, String phone);
}

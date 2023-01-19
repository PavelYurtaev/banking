package com.pyurtaev.banking.service;

import com.pyurtaev.banking.model.EmailData;
import com.pyurtaev.banking.model.User;

public interface EmailDataService {

    EmailData getById(Long id);

    EmailData getByEmail(String email);

    Long createEmailData(User user, String email);

    void updateEmailData(Long emailDataId, String email);
}

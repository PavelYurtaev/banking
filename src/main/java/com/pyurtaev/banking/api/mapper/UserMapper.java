package com.pyurtaev.banking.api.mapper;

import com.pyurtaev.banking.api.dto.UserDto;
import com.pyurtaev.banking.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {

    // todo ModelMapper
    public UserDto mapToDto(final User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .build();
        // todo join email and phone
    }
}

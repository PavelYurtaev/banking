package com.pyurtaev.banking.api;

import com.pyurtaev.banking.api.dto.EmailDataDto;
import com.pyurtaev.banking.api.dto.MoneyTransferDto;
import com.pyurtaev.banking.api.dto.PhoneDataDto;
import com.pyurtaev.banking.api.dto.UserDto;
import com.pyurtaev.banking.api.mapper.UserMapper;
import com.pyurtaev.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/user")
    public Page<UserDto> searchUsers(
            Pageable pageable,
            @RequestParam(required = false) LocalDate dateOfBirth,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email) {
        return userService.searchUsers(pageable, dateOfBirth, name, phone, email).map(userMapper::mapToDto);
    }

    @PostMapping("/user/{userId}/email")
    public ResponseEntity<Long> createUserEmail(@NotEmpty @PathVariable Long userId,
            @Valid @RequestBody EmailDataDto dto) {
        return ResponseEntity.ok(userService.createUserEmail(userId, dto.getEmail()));
    }

    @PostMapping("/user/{userId}/phone")
    public ResponseEntity<Long> createUserPhone(@NotEmpty @PathVariable Long userId,
            @Valid @RequestBody PhoneDataDto dto) {
        return ResponseEntity.ok(userService.createUserPhone(userId, dto.getPhone()));
    }

    @PutMapping("/user/email/{emailDataId}")
    public ResponseEntity<String> updateUserEmail(
            @NotEmpty @PathVariable Long emailDataId,
            @Valid @RequestBody EmailDataDto dto) {
        userService.updateUserEmail(emailDataId, dto.getEmail());
        return ResponseEntity.ok("");
    }

    @PutMapping("/user/phone/{phoneDataId}")
    public ResponseEntity<String> updateUserPhone(
            @NotEmpty @PathVariable Long phoneDataId,
            @Valid @RequestBody PhoneDataDto dto) {
        userService.updateUserPhone(phoneDataId, dto.getPhone());
        return ResponseEntity.ok("");
    }

    @PutMapping("/user/transfer_money")
    public ResponseEntity<String> transferMoney(Principal principal,
            @Valid @RequestBody MoneyTransferDto dto) {
        final Long userIdTransferFrom = Long.valueOf(principal.getName());
        userService.transferMoney(userIdTransferFrom, dto.getUserIdTransferTo(), dto.getValue());
        return ResponseEntity.ok("");
    }


}

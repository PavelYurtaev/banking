package com.pyurtaev.banking.api;

import com.pyurtaev.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final UserService userService;

    @GetMapping("/token")
    public ResponseEntity<String> getJwtToken(@RequestHeader String email, @RequestHeader String password) {
        return ResponseEntity.ok(userService.getUserToken(email, password));
    }
}

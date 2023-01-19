package com.pyurtaev.banking.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class BankingControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(final Exception exception) {
        log.error("Handling general API exception", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
    }

    @ExceptionHandler(BankingException.class)
    public ResponseEntity<String> handleBankingException(final BankingException exception) {
        log.error("Handling BankingException", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler({javax.validation.ConstraintViolationException.class,
            org.hibernate.exception.ConstraintViolationException.class, DataIntegrityViolationException.class})
    public ResponseEntity<String> handleValidationException(final Exception exception) {
        log.error("Handling validation exception", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation failed");
    }
}

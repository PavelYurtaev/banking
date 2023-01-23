package com.pyurtaev.banking.service.impl;

import com.pyurtaev.banking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AccumulationScheduler {

    public static Map<Long, BigDecimal> balanceAutoIncreaseLimits = new ConcurrentHashMap<>();

    private final UserService userService;

    @Scheduled(initialDelay = 2000, fixedDelay = 30000)
    public void addPercentsToAccounts() {
        userService.addPercentsToAccounts();
    }

    public void registerUserLimit(final Long userId, final BigDecimal value) {
        final BigDecimal percent207Limit = value.add(
                value.multiply(BigDecimal.valueOf(207)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        balanceAutoIncreaseLimits.put(userId, percent207Limit);
    }
}

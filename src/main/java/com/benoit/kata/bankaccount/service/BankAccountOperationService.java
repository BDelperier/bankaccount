package com.benoit.kata.bankaccount.service;

import com.benoit.kata.bankaccount.model.enums.ActionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankAccountOperationService {

    public BigDecimal computeOperation(ActionTypeEnum actionType, BigDecimal oldBalance, BigDecimal amount) {
        BigDecimal newBalance;
        switch (actionType) {
            case DEPOSIT:
                newBalance = oldBalance.add(amount);
                break;
            case WITHDRAWAL:
                newBalance = oldBalance.subtract(amount);
                break;
            default:
                throw new IllegalStateException("action type not defined");
        }
        return newBalance;
    }
}

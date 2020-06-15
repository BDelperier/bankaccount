package com.benoit.kata.bankaccount.service;

import com.benoit.kata.bankaccount.model.enums.ActionTypeEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;


@ExtendWith(MockitoExtension.class)
public class BankAccountOperationServiceTest {

    @InjectMocks
    private BankAccountOperationService bankAccountOperationService;

    @ParameterizedTest
    @MethodSource("provideComputeArguments")
    public void compute_should_add_when_deposit(ActionTypeEnum actionTypeEnum, BigDecimal oldBalance, BigDecimal amount, BigDecimal expectedBalance) {
        //when
        BigDecimal newBalance = bankAccountOperationService.computeOperation(actionTypeEnum, oldBalance, amount);

        //then
        Assertions.assertThat(newBalance).isEqualTo(expectedBalance);
    }

    private static Stream<Arguments> provideComputeArguments() {
        return Stream.of(
                Arguments.of(ActionTypeEnum.DEPOSIT, new BigDecimal("1001.01"), new BigDecimal("152.34"), new BigDecimal("1153.35")),
                Arguments.of(ActionTypeEnum.WITHDRAWAL, new BigDecimal("1153.35"), new BigDecimal("152.34"), new BigDecimal("1001.01")),
                Arguments.of(ActionTypeEnum.WITHDRAWAL, new BigDecimal("10.15"), new BigDecimal("10.15"), new BigDecimal("0.00")),
                Arguments.of(ActionTypeEnum.WITHDRAWAL, new BigDecimal("10.14"), new BigDecimal("10.15"), new BigDecimal("-0.01")),
                Arguments.of(ActionTypeEnum.DEPOSIT, new BigDecimal("-10"), new BigDecimal("14.54"), new BigDecimal("4.54"))
        );
    }
}

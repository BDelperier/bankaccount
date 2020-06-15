package com.benoit.kata.bankaccount.model.enums;

import com.benoit.kata.bankaccount.entity.enums.OperationTypeEntityEnum;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ActionTypeEnum {
    DEPOSIT(OperationTypeEntityEnum.DEPOSIT),
    WITHDRAWAL(OperationTypeEntityEnum.WITHDRAWAL);

    private final OperationTypeEntityEnum operationTypeEntityEnum;

    ActionTypeEnum(OperationTypeEntityEnum operationTypeEntityEnum) {
        this.operationTypeEntityEnum = operationTypeEntityEnum;
    }

    public OperationTypeEntityEnum toOperationType() {
        return operationTypeEntityEnum;
    }

    public static ActionTypeEnum fromOperationTypeEnum(OperationTypeEntityEnum operationType) {
        return Arrays.stream(ActionTypeEnum.values())
                .filter(actionTypeEnum -> operationType == actionTypeEnum.getOperationTypeEntityEnum())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Error mapping enum "+operationType.name()));
    }
}

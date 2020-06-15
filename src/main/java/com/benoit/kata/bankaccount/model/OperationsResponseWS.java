package com.benoit.kata.bankaccount.model;

import com.benoit.kata.bankaccount.model.enums.ActionTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@ApiModel
public class OperationsResponseWS {

    Instant dateOperation;

    BigDecimal amount;

    BigDecimal balanceBefore;

    ActionTypeEnum actionType;

}

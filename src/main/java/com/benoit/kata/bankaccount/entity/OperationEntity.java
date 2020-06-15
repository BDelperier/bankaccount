package com.benoit.kata.bankaccount.entity;

import com.benoit.kata.bankaccount.entity.enums.OperationTypeEntityEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OperationEntity {

    UUID id;

    BigDecimal amount;

    OperationTypeEntityEnum type;

    BigDecimal balanceBefore;

    Instant date;

    String userName;

}

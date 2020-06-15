package com.benoit.kata.bankaccount.model;


import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@ApiModel
public class ActionResponseWS {

    BigDecimal balanceAfter;

    UUID operationId;

}

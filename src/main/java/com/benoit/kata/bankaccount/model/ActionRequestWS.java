package com.benoit.kata.bankaccount.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@ApiModel
public class ActionRequestWS {

    @ApiModelProperty(value = "the amount")
    @PositiveOrZero
    BigDecimal amount;
}

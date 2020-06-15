package com.benoit.kata.bankaccount.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {

    UUID id;

    String name;

}

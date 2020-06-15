package com.benoit.kata.bankaccount.repository;

import com.benoit.kata.bankaccount.entity.AccountEntity;
import com.benoit.kata.bankaccount.entity.OperationEntity;
import com.benoit.kata.bankaccount.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class GlobalCacheRepository {

    private final List<AccountEntity> accountEntities = new ArrayList<>();
    private final List<OperationEntity> operationEntities = new ArrayList<>();
    private final List<UserEntity> userEntities = new ArrayList<>();

    public Optional<AccountEntity> findAccountByUserName(String username) {
        return accountEntities.stream()
                .filter(accountEntity -> accountEntity.getUserEntity().getName().equals(username))
                .findFirst();
    }

    public OperationEntity addOperationEntity(OperationEntity operationEntity) {
        this.operationEntities.add(operationEntity);
        return operationEntity;
    }

    public List<OperationEntity> findOperations(String username) {
        return this.operationEntities.stream()
                .filter(operationEntity -> operationEntity.getUserName().equals(username))
                .sorted(Comparator.comparing(OperationEntity::getDate))
                .collect(Collectors.toList());
    }


    /**
     * Method to init data in tests
     * @param userName the userName to create if necessary
     * @param balance the balance to set
     */
    public void initUser(String userName, BigDecimal balance) {
        if (this.userEntities.stream()
                    .noneMatch(userEntity -> userEntity.getName().equals(userName))) {
            this.userEntities.add(UserEntity.builder()
                    .id(UUID.randomUUID())
                    .name(userName)
                    .build());
        }

        if (accountEntities.stream()
                .noneMatch(accountEntity -> accountEntity.getUserEntity().getName().equals(userName))) {
            //create
            UserEntity userEntity = getUserEntity(userName);
            accountEntities.add((AccountEntity.builder().id(UUID.randomUUID()).balance(balance).userEntity(userEntity).build()));
        }
    }

    private UserEntity getUserEntity(String userName) {
        return userEntities.stream()
                .filter(uEntity -> uEntity.getName().equals(userName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot inf user entity from name "+userName));
    }
}

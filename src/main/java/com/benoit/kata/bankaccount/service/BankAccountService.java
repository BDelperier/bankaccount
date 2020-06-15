package com.benoit.kata.bankaccount.service;

import com.benoit.kata.bankaccount.entity.AccountEntity;
import com.benoit.kata.bankaccount.entity.OperationEntity;
import com.benoit.kata.bankaccount.exception.AccountNotFound;
import com.benoit.kata.bankaccount.model.ActionRequestWS;
import com.benoit.kata.bankaccount.model.ActionResponseWS;
import com.benoit.kata.bankaccount.model.enums.ActionTypeEnum;
import com.benoit.kata.bankaccount.model.OperationsResponseWS;
import com.benoit.kata.bankaccount.repository.GlobalCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountOperationService bankAccountOperationService;
    private final GlobalCacheRepository globalCacheRepository;

    /**
     * Execute the required action.
     * Log the action in the list of operations.
     *
     * @param username the username
     * @param request the action details to execute
     * @param actionType the action type
     * @return the details of the action executed
     * @throws AccountNotFound if the account does not exists for the given userName
     */
    public ActionResponseWS action(String username, ActionRequestWS request, ActionTypeEnum actionType) {
        AccountEntity accountEntity = globalCacheRepository
                .findAccountByUserName(username)
                .orElseThrow(() -> new AccountNotFound(username));

        OperationEntity operationEntity = OperationEntity.builder()
                .id(UUID.randomUUID())
                .date(Instant.now())
                .type(actionType.toOperationType())
                .amount(request.getAmount())
                .balanceBefore(accountEntity.getBalance())
                .userName(username)
                .build();
        globalCacheRepository.addOperationEntity(operationEntity);


        accountEntity.setBalance(
                bankAccountOperationService.computeOperation(actionType, accountEntity.getBalance(), request.getAmount()));

        return ActionResponseWS.builder()
                .balanceAfter(accountEntity.getBalance())
                .operationId(operationEntity.getId())
                .build();
    }

    /**
     * Find the operations for the given user name
     *
     * @param username the userName
     * @return the list of Operations found
     */
    public List<OperationsResponseWS> findOperations(String username) {
        return globalCacheRepository.findOperations(username).stream()
                .map(operationEntity -> OperationsResponseWS.builder()
                        .amount(operationEntity.getAmount())
                        .balanceBefore(operationEntity.getBalanceBefore())
                        .dateOperation(operationEntity.getDate())
                        .actionType(ActionTypeEnum.fromOperationTypeEnum(operationEntity.getType()))
                        .build()
                )
                .collect(Collectors.toList());
    }

}

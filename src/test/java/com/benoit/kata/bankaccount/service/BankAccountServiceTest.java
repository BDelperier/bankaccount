package com.benoit.kata.bankaccount.service;

import com.benoit.kata.bankaccount.entity.AccountEntity;
import com.benoit.kata.bankaccount.entity.OperationEntity;
import com.benoit.kata.bankaccount.entity.UserEntity;
import com.benoit.kata.bankaccount.entity.enums.OperationTypeEntityEnum;
import com.benoit.kata.bankaccount.model.ActionRequestWS;
import com.benoit.kata.bankaccount.model.ActionResponseWS;
import com.benoit.kata.bankaccount.model.enums.ActionTypeEnum;
import com.benoit.kata.bankaccount.repository.GlobalCacheRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private GlobalCacheRepository globalCacheRepository;

    @Mock
    private BankAccountOperationService bankAccountOperationService;

    @Captor
    private ArgumentCaptor<OperationEntity> captorOperationEntity;

    @InjectMocks
    private BankAccountService bankAccountService;


    @Test
    public void action_deposit_should_add_operation() {
        //given
        String userName = "benoit";
        ActionRequestWS actionRequestWS = ActionRequestWS.builder()
                .amount(new BigDecimal("67"))
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name(userName)
                .build();
        AccountEntity accountEntity = AccountEntity.builder()
                .id(UUID.randomUUID())
                .balance(new BigDecimal("1000"))
                .userEntity(userEntity)
                .build();

        when(globalCacheRepository.findAccountByUserName(userName)).thenReturn(Optional.of(accountEntity));
        when(bankAccountOperationService.computeOperation(ActionTypeEnum.DEPOSIT, accountEntity.getBalance(), actionRequestWS.getAmount()))
                .thenReturn(new BigDecimal("1067"));


        //when
        ActionResponseWS actionResponseWS = bankAccountService.action(userName, actionRequestWS, ActionTypeEnum.DEPOSIT);

        //then
        Assertions.assertThat(actionResponseWS).isNotNull();
        Assertions.assertThat(actionResponseWS.getBalanceAfter()).isEqualTo(new BigDecimal("1067"));

        verify(globalCacheRepository).addOperationEntity(captorOperationEntity.capture());
        OperationEntity operationEntityAdded = captorOperationEntity.getValue();

        Assertions.assertThat(operationEntityAdded).isNotNull();
        Assertions.assertThat(operationEntityAdded.getDate()).isNotNull().isAfter(Instant.now().minusSeconds(60));
        Assertions.assertThat(operationEntityAdded.getAmount()).isEqualTo(new BigDecimal("67"));
        Assertions.assertThat(operationEntityAdded.getBalanceBefore()).isEqualTo(new BigDecimal("1000"));
        Assertions.assertThat(operationEntityAdded.getUserName()).isEqualTo(userName);
        Assertions.assertThat(operationEntityAdded.getType()).isEqualTo(OperationTypeEntityEnum.DEPOSIT);
    }

    @Test
    public void action_withdrawal_should_add_operation() {
        //given
        String userName = "benoit";
        ActionRequestWS actionRequestWS = ActionRequestWS.builder()
                .amount(new BigDecimal("67"))
                .build();

        UserEntity userEntity = UserEntity.builder()
                .id(UUID.randomUUID())
                .name(userName)
                .build();
        AccountEntity accountEntity = AccountEntity.builder()
                .id(UUID.randomUUID())
                .balance(new BigDecimal("1000"))
                .userEntity(userEntity)
                .build();

        when(globalCacheRepository.findAccountByUserName(userName)).thenReturn(Optional.of(accountEntity));
        when(bankAccountOperationService.computeOperation(ActionTypeEnum.WITHDRAWAL, accountEntity.getBalance(), actionRequestWS.getAmount()))
                .thenReturn(new BigDecimal("933"));


        //when
        ActionResponseWS actionResponseWS = bankAccountService.action(userName, actionRequestWS, ActionTypeEnum.WITHDRAWAL);

        //then
        Assertions.assertThat(actionResponseWS).isNotNull();
        Assertions.assertThat(actionResponseWS.getBalanceAfter()).isEqualTo(new BigDecimal("933"));

        verify(globalCacheRepository).addOperationEntity(captorOperationEntity.capture());
        OperationEntity operationEntityAdded = captorOperationEntity.getValue();

        Assertions.assertThat(operationEntityAdded).isNotNull();
        Assertions.assertThat(operationEntityAdded.getDate()).isNotNull().isAfter(Instant.now().minusSeconds(60));
        Assertions.assertThat(operationEntityAdded.getAmount()).isEqualTo(new BigDecimal("67"));
        Assertions.assertThat(operationEntityAdded.getBalanceBefore()).isEqualTo(new BigDecimal("1000"));
        Assertions.assertThat(operationEntityAdded.getUserName()).isEqualTo(userName);
        Assertions.assertThat(operationEntityAdded.getType()).isEqualTo(OperationTypeEntityEnum.WITHDRAWAL);
    }

}

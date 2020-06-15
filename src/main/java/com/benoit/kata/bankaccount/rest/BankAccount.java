package com.benoit.kata.bankaccount.rest;

import com.benoit.kata.bankaccount.model.ActionRequestWS;
import com.benoit.kata.bankaccount.model.ActionResponseWS;
import com.benoit.kata.bankaccount.model.OperationsResponseWS;
import com.benoit.kata.bankaccount.model.enums.ActionTypeEnum;
import com.benoit.kata.bankaccount.service.BankAccountService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
public class BankAccount {

    private final BankAccountService bankAccountService;

    @ApiOperation(value = "Make a deposit on the current user account")
    @PostMapping(path = "/bankaccount/deposit/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ActionResponseWS deposit(
            /* @AuthenticationPrincipal UserDetails userDetails, */
            @PathVariable String userName,
            @Valid @RequestBody ActionRequestWS actionRequestWS) {
        log.debug("deposit for user {} :{}.", userName, actionRequestWS.getAmount());

        return bankAccountService
                .action(userName, actionRequestWS, ActionTypeEnum.DEPOSIT);
    }

    @ApiOperation(value = "Make a withdrawal on the current user account")
    @PostMapping(path = "/bankaccount/withdrawal/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ActionResponseWS withdrawal(
            /* @AuthenticationPrincipal UserDetails userDetails, */
            @PathVariable String userName,
            @Valid @RequestBody ActionRequestWS actionRequestWS) {
        log.debug("withdrawal for user {} :{}.", userName, actionRequestWS.getAmount());

        return bankAccountService
                .action(userName, actionRequestWS, ActionTypeEnum.WITHDRAWAL);
    }

    @ApiOperation(value = "List all Operations on the current user")
    @GetMapping(path = "/bankaccount/operations/{userName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OperationsResponseWS> listOperations(
            /* @AuthenticationPrincipal UserDetails userDetails, */
            @PathVariable String userName) {
        log.debug("list operations for user {}.", userName);

        return bankAccountService
                .findOperations(userName);
    }
}

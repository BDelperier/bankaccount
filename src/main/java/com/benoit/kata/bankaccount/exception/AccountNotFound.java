package com.benoit.kata.bankaccount.exception;

public class AccountNotFound extends RuntimeException {
    public AccountNotFound(String username) {
        super(username);
    }
}

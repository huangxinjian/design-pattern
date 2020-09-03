package com.wallet.base.exception;

public class NoSufficientBalanceException extends Throwable {
    public NoSufficientBalanceException(String s) {
        System.out.println(s);
    }
}

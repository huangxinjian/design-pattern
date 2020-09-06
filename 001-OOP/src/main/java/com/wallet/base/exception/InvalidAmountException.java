package com.wallet.base.exception;

public class InvalidAmountException extends Throwable {
    public InvalidAmountException(String msg) {
        System.out.println(msg);
    }
}

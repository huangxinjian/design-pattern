package com.wallet.base.entity;

import com.wallet.base.status.Status;

import java.math.BigDecimal;

public class VirtualWalletTransactionEntity {
    private BigDecimal amount;
    private long createTime;
    private Long fromWalletId;
    private Long toWalletId;
    private Status status;

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public void setFromWalletId(Long fromWalletId) {
        this.fromWalletId = fromWalletId;
    }

    public void setToWalletId(Long toWalletId) {
        this.toWalletId = toWalletId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

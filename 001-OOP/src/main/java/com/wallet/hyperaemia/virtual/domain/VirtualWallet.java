package com.wallet.hyperaemia.virtual.domain;

import com.wallet.base.exception.InvalidAmountException;
import com.wallet.base.exception.NoSufficientBalanceException;

import java.math.BigDecimal;

/**
 * 充血领域模型
 *
 * 主要业务功能实现在这里，并且这里不与 repository 交互，交给 service 交互
 */
public class VirtualWallet {

    private Long id;
    // 原来的时间在构造函数中由 service 构建并注入，现在在 Domain 中注入
    private Long createTime = System.currentTimeMillis();
    private BigDecimal balance;

    /**
     * 冻结和透支功能
     */
    private boolean isAllowedOverdraft = true;
    private BigDecimal overdraftAmount = BigDecimal.ZERO;
    private BigDecimal frozenAmount = BigDecimal.ZERO;

    /**
     * 这里的 id 可以搭配一些 生产策略来生成
     * @param preAllocatedId
     */
    public VirtualWallet(Long preAllocatedId) {
        this.id = preAllocatedId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 支出
     * @param amount
     */
    public void debit(BigDecimal amount) throws NoSufficientBalanceException {
        if (this.balance.compareTo(amount) < 0) {
            throw new NoSufficientBalanceException("余额不足");
        }
        this.balance.subtract(amount);
    }

    /**
     * 金额添加
     * @param amount
     * @throws InvalidAmountException
     */
    public void credit(BigDecimal amount) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("非法金额");
        }
        this.balance.add(amount);
    }


}

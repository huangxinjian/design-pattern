package com.wallet.base.repository;

import com.wallet.base.entity.VirtualWalletEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * 负责虚拟钱包余额记录
 */
@Repository
public interface VirtualWalletRepository {
    VirtualWalletEntity getWalletEntity(Long walletId);

    BigDecimal getBalance(Long walletId);

    void updateBalance(Long walletId, BigDecimal amount);
}

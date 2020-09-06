package com.wallet.base.repository;

import com.wallet.base.entity.VirtualWalletTransactionEntity;
import com.wallet.base.status.Status;
import org.springframework.stereotype.Repository;

/**
 * 负责交易流水记录
 */
@Repository
public interface VirtualWalletTransactionRepository {
    Long saveTransaction(VirtualWalletTransactionEntity transactionEntity);

    void updateStatus(Long transactionId, Status status);
}

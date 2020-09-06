package com.wallet.hyperaemia.virtual.service.impl;

import com.wallet.anemia.virtual.entity.VirtualWalletBO;
import com.wallet.base.entity.VirtualWalletEntity;
import com.wallet.base.entity.VirtualWalletTransactionEntity;
import com.wallet.base.exception.InvalidAmountException;
import com.wallet.base.exception.NoSufficientBalanceException;
import com.wallet.base.repository.VirtualWalletRepository;
import com.wallet.base.repository.VirtualWalletTransactionRepository;
import com.wallet.base.status.Status;
import com.wallet.hyperaemia.virtual.domain.VirtualWallet;
import com.wallet.hyperaemia.virtual.service.VirtualWalletService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * 在充血模型中，service是很轻的
 * 主要通过调用 domain 模型中的方法来实现业务，是一种纯面向对象的思想
 */
public class VirtualWalletServiceImpl implements VirtualWalletService {

    @Autowired
    private VirtualWalletRepository walletRepo;

    @Autowired
    private VirtualWalletTransactionRepository transactionRepo;

    /**
     * 获取虚拟钱包对象
     * @param walletId 虚拟钱包 id
     * @return 虚拟钱包
     */
    public VirtualWallet getVirtualWallet(Long walletId) {
        VirtualWalletEntity walletEntity = walletRepo.getWalletEntity(walletId);
        VirtualWallet wallet = convert(walletEntity);
        return wallet;
    }

    /**
     * 读取余额
     * @param walletId 虚拟钱包 id
     * @return 余额
     */
    public BigDecimal getBalance(Long walletId) {
        return walletRepo.getBalance(walletId);
    }

    /**
     * 取款
     * @param walletId 虚拟钱包 id
     * @param amount 取款金额
     * @throws NoSufficientBalanceException 余额不足异常
     */
    public void debit(Long walletId, BigDecimal amount) throws NoSufficientBalanceException {
        VirtualWalletEntity walletEntity = walletRepo.getWalletEntity(walletId);
        VirtualWallet wallet = convert(walletEntity);
        // 调用充血模型业务方法
        wallet.debit(amount);
        walletRepo.updateBalance(walletId, wallet.getBalance());
    }

    /**
     * 增加余额
     * @param walletId
     * @param amount
     */
    public void credit(Long walletId, BigDecimal amount) throws InvalidAmountException {
        VirtualWalletEntity walletEntity = walletRepo.getWalletEntity(walletId);
        VirtualWallet wallet = convert(walletEntity);
        // 调用充血模型业务方法
        wallet.credit(amount);
        walletRepo.updateBalance(walletId, wallet.getBalance());
    }

    public void transfer(Long fromWalletId, Long toWalletId, BigDecimal amount){
        VirtualWalletTransactionEntity transactionEntity = new VirtualWalletTransactionEntity();
        transactionEntity.setAmount(amount);
        transactionEntity.setCreateTime(System.currentTimeMillis());
        transactionEntity.setFromWalletId(fromWalletId);
        transactionEntity.setToWalletId(toWalletId);
        transactionEntity.setStatus(Status.TO_BE_EXECUTED);
        // 保存 “待执行” 状态的交易记录
        Long transactionId = transactionRepo.saveTransaction(transactionEntity);
        try {
            this.debit(fromWalletId, amount);
            this.credit(toWalletId, amount);
        } catch (NoSufficientBalanceException e) {
            transactionRepo.updateStatus(transactionId, Status.CLOSED);
            return;
        } catch (Exception | InvalidAmountException e) {
            transactionRepo.updateStatus(transactionId, Status.FAILED);
            return;
        }
        transactionRepo.updateStatus(transactionId, Status.EXECUTED);
    }


    private VirtualWallet convert(VirtualWalletEntity walletEntity) {
        return null;
    }
}

package com.wallet.anemia.virtual.service.impl;

import com.wallet.anemia.virtual.entity.VirtualWalletBO;
import com.wallet.base.entity.VirtualWalletEntity;
import com.wallet.base.entity.VirtualWalletTransactionEntity;
import com.wallet.base.exception.NoSufficientBalanceException;
import com.wallet.base.repository.VirtualWalletRepository;
import com.wallet.base.repository.VirtualWalletTransactionRepository;
import com.wallet.anemia.virtual.service.VirtualWalletService;
import com.wallet.base.status.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
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
    public VirtualWalletBO getVirtualWallet(Long walletId) {
        VirtualWalletEntity walletEntity = walletRepo.getWalletEntity(walletId);
        VirtualWalletBO walletBo = convert(walletEntity);
        return walletBo;
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
        // 更新余额：这些都属于业务操作，在 DDD 中会抽离到充血模型中
        BigDecimal balance = walletEntity.getBalance();
        if (balance.compareTo(amount) < 0) {
            throw new NoSufficientBalanceException("余额不足，操作失败！");
        }
        BigDecimal res = balance.subtract(amount);
        walletRepo.updateBalance(walletId, res);
    }

    /**
     * 增加余额
     * @param walletId
     * @param amount
     */
    public void credit(Long walletId, BigDecimal amount) {
        VirtualWalletEntity walletEntity = walletRepo.getWalletEntity(walletId);
        // 这一行就是业务代码，获取余额并添加, 可以抽离到 Domain 中
        BigDecimal balance = walletEntity.getBalance();
        BigDecimal res = balance.add(amount);
        // 更新余额，在 DDD 中抽离到 Domain 中
        walletRepo.updateBalance(walletId, res);
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
        } catch (Exception e) {
            transactionRepo.updateStatus(transactionId, Status.FAILED);
            return;
        }
        transactionRepo.updateStatus(transactionId, Status.EXECUTED);
    }


    private VirtualWalletBO convert(VirtualWalletEntity walletEntity) {
        return null;
    }

}

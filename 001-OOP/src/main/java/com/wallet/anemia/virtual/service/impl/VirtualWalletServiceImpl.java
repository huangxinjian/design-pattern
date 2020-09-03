package com.wallet.anemia.virtual.service.impl;

import com.wallet.anemia.virtual.entity.VirtualWalletBO;
import com.wallet.base.entity.VirtualWalletEntity;
import com.wallet.base.exception.NoSufficientBalanceException;
import com.wallet.base.repository.VirtualWalletRepository;
import com.wallet.base.repository.VirtualWalletTransactionRepository;
import com.wallet.anemia.virtual.service.VirtualWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class VirtualWalletServiceImpl implements VirtualWalletService {

    @Autowired
    private VirtualWalletRepository walletRepo;

    @Autowired
    private VirtualWalletTransactionRepository WalletTransactionRepository;

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
        BigDecimal balance = walletEntity.getBalance();
        if (balance.compareTo(amount) < 0) {
            throw new NoSufficientBalanceException("余额不足，操作失败！");
        }
        // 更新余额
        walletRepo.updateBalance(walletId, balance.subtract(amount));
    }

    private VirtualWalletBO convert(VirtualWalletEntity walletEntity) {
        return null;
    }

}

package com.wallet.anemia.virtual.service.impl;

import com.wallet.anemia.virtual.repository.VirtualWalletRepository;
import com.wallet.anemia.virtual.repository.VirtualWalletTransactionRepository;
import com.wallet.anemia.virtual.service.VirtualWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VirtualWalletServiceImpl implements VirtualWalletService {

    @Autowired
    private VirtualWalletRepository virtualWalletRepository;

    @Autowired
    private VirtualWalletTransactionRepository virtualWalletTransactionRepository;
}

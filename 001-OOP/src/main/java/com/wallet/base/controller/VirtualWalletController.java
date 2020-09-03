package com.wallet.base.controller;


import com.wallet.anemia.virtual.service.VirtualWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VirtualWalletController {

    @Autowired
    private VirtualWalletService virtualWalletService;
}

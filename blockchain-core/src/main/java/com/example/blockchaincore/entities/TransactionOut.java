package com.example.blockchaincore.entities;

import com.example.blockchaincore.api.BlockData;

public class TransactionOut {

    private String toAddress;               //一个指明发送者地址的 ECDSA 的公钥，这意味着用户拥有所引用的公钥的私钥
    private BlockData info;                //交易内容，比如金币数量

    public TransactionOut(String toAddress, BlockData info) {
        this.toAddress = toAddress;
        this.info = info;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BlockData getInfo() {
        return info;
    }

    public void setInfo(BlockData info) {
        this.info = info;
    }
}

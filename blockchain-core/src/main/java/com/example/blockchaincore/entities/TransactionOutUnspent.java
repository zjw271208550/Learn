package com.example.blockchaincore.entities;

import com.example.blockchaincore.api.BlockData;

public class TransactionOutUnspent {
    private String senderAddress;
    private BlockData info;
    private String outFromAddress;
    private int outFromIndex;

    public TransactionOutUnspent(String senderAddress, BlockData info, String outFromAddress, int outFromIndex) {
        this.senderAddress = senderAddress;
        this.info = info;
        this.outFromAddress = outFromAddress;
        this.outFromIndex = outFromIndex;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public BlockData getInfo() {
        return info;
    }

    public void setInfo(BlockData info) {
        this.info = info;
    }

    public String getOutFromAddress() {
        return outFromAddress;
    }

    public void setOutFromAddress(String outFromAddress) {
        this.outFromAddress = outFromAddress;
    }

    public int getOutFromIndex() {
        return outFromIndex;
    }

    public void setOutFromIndex(int outFromIndex) {
        this.outFromIndex = outFromIndex;
    }
}

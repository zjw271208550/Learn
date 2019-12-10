package com.example.blockchaincore.entities;

import com.example.blockchaincore.api.BlockData;

public class TransactionOutUnspent {
    private String toAddress;//to reciver
    private BlockData info;
    private String outFromTxId;
    private int outFromIndex;

    public TransactionOutUnspent(String toAddress, BlockData info, String outFromTxId, int outFromIndex) {
        this.toAddress = toAddress;
        this.info = info;
        this.outFromTxId = outFromTxId;
        this.outFromIndex = outFromIndex;
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

    public String getOutFromTxId() {
        return outFromTxId;
    }

    public void setOutFromTxId(String outFromTxId) {
        this.outFromTxId = outFromTxId;
    }

    public int getOutFromIndex() {
        return outFromIndex;
    }

    public void setOutFromIndex(int outFromIndex) {
        this.outFromIndex = outFromIndex;
    }
}

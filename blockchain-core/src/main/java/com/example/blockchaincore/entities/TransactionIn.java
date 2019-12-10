package com.example.blockchaincore.entities;

public class TransactionIn {
    private String outFromTxId;
    private int outFromIndex;
    private String signature;

    public TransactionIn(String outFromTxId, int outFromIndex, String signature) {
        this.outFromTxId = outFromTxId;
        this.outFromIndex = outFromIndex;
        this.signature = signature;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

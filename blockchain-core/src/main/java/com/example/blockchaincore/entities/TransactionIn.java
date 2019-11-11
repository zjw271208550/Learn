package com.example.blockchaincore.entities;

public class TransactionIn {
    private String outFromAddress;
    private int outFromIndex;
    private String signature;

    public TransactionIn(String outFromAddress, int outFromIndex, String signature) {
        this.outFromAddress = outFromAddress;
        this.outFromIndex = outFromIndex;
        this.signature = signature;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}

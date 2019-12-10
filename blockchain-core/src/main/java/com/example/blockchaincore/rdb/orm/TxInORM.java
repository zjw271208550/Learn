package com.example.blockchaincore.rdb.orm;

public class TxInORM {
    private String txId;
    private String preTxId;
    private int preTxIdx;
    private String sign;

    public TxInORM(String txId, String preTxId, int preTxIdx, String sign) {
        this.txId = txId;
        this.preTxId = preTxId;
        this.preTxIdx = preTxIdx;
        this.sign = sign;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getPreTxId() {
        return preTxId;
    }

    public void setPreTxId(String preTxId) {
        this.preTxId = preTxId;
    }

    public int getPreTxIdx() {
        return preTxIdx;
    }

    public void setPreTxIdx(int preTxIdx) {
        this.preTxIdx = preTxIdx;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}

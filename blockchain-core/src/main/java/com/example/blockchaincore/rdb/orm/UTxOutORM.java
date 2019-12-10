package com.example.blockchaincore.rdb.orm;

public class UTxOutORM {
    private String publicKey;
    private String dataId;
    private String data;
    private String txId;
    private int txIdx;

    public UTxOutORM(String publicKey, String dataId, String data, String txId, int txIdx) {
        this.publicKey = publicKey;
        this.dataId = dataId;
        this.data = data;
        this.txId = txId;
        this.txIdx = txIdx;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public int getTxIdx() {
        return txIdx;
    }

    public void setTxIdx(int txIdx) {
        this.txIdx = txIdx;
    }
}

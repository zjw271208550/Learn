package com.example.blockchaincore.rdb.orm;

public class TxOutORM {
    private String txId;
    private String addressTo;
    private String dataId;
    private String data;

    public TxOutORM(String txId, String addressTo, String dataId, String data) {
        this.txId = txId;
        this.addressTo = addressTo;
        this.dataId = dataId;
        this.data = data;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public String getAddressTo() {
        return addressTo;
    }

    public void setAddressTo(String addressTo) {
        this.addressTo = addressTo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

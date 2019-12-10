package com.example.blockchaincore.rdb.orm;

import java.util.List;

public class TxORM {
    private Integer blockIdx;
    private String id;
    private String fromAddress;
    private List<TxInORM> txInORMS;
    private List<TxOutORM> txOutORMS;

    public TxORM(Integer blockIdx, String id, String fromAddress, List<TxInORM> txInORMS, List<TxOutORM> txOutORMS) {
        this.blockIdx = blockIdx;
        this.id = id;
        this.fromAddress = fromAddress;
        this.txInORMS = txInORMS;
        this.txOutORMS = txOutORMS;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public TxORM(Integer blockIdx, String fromAddress, String id) {
        this.blockIdx = blockIdx;
        this.fromAddress = fromAddress;
        this.id = id;
    }

    public Integer getBlockIdx() {
        return blockIdx;
    }

    public void setBlockIdx(Integer blockIdx) {
        this.blockIdx = blockIdx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TxInORM> getTxInORMS() {
        return txInORMS;
    }

    public void setTxInORMS(List<TxInORM> txInORMS) {
        this.txInORMS = txInORMS;
    }

    public List<TxOutORM> getTxOutORMS() {
        return txOutORMS;
    }

    public void setTxOutORMS(List<TxOutORM> txOutORMS) {
        this.txOutORMS = txOutORMS;
    }
}

package com.example.blockchaincore.entities;

import com.example.blockchaincore.funcations.HashFunction;

import java.util.List;

public class TransactionBody {
    private String id;
    private List<TransactionIn> txIns;
    private List<TransactionOut> txOuts;

    public TransactionBody(List<TransactionIn> txIns, List<TransactionOut> txOuts) {
        this.txIns = txIns;
        this.txOuts = txOuts;
        this.generateId();
    }

    public String getId() {
        return id;
    }

    public List<TransactionIn> getTxIns() {
        return txIns;
    }

    public List<TransactionOut> getTxOuts() {
        return txOuts;
    }

}

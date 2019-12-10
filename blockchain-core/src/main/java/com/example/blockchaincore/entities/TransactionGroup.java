package com.example.blockchaincore.entities;

import com.example.blockchaincore.funcations.HashFunction;
import com.example.blockchaincore.funcations.Transaction;

import java.util.List;

public class TransactionGroup {
    private String id;
    private String fromAddress;
    private List<TransactionIn> txIns;
    private List<TransactionOut> txOuts;

    public TransactionGroup(String fromAddress,List<TransactionIn> txIns, List<TransactionOut> txOuts) {
        this.fromAddress = fromAddress;
        this.txIns = txIns;
        this.txOuts = txOuts;
        this.id = Transaction.generateId(this);
    }

    public TransactionGroup(String id, String fromAddress, List<TransactionIn> txIns, List<TransactionOut> txOuts) {
        this.id = id;
        this.fromAddress = fromAddress;
        this.txIns = txIns;
        this.txOuts = txOuts;
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

    public String getFromAddress() {
        return fromAddress;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("{id:'"+this.id+"',fromAddress:'"+this.fromAddress+"',txIns:[");
        for(TransactionIn txIn:this.txIns){
            builder.append("{txId:'"+txIn.getOutFromTxId());
            builder.append("',txIndex:"+txIn.getOutFromIndex());
            builder.append(",sign:'"+txIn.getSignature()+"'},");
        }
        if(!this.txIns.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("],txOuts:[");
        for(TransactionOut txOut:this.txOuts){
            builder.append("{toAddress:'"+txOut.getToAddress());
            builder.append("',dataId:'"+txOut.getInfo().getDataId());
            builder.append("',data:'"+txOut.getInfo().getSHA256Data()+"'},");
        }
        if(!this.txIns.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        builder.append("]}");
        return builder.toString();
    }
}

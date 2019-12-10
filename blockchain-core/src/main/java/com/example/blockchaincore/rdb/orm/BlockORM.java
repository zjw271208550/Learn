package com.example.blockchaincore.rdb.orm;

import java.sql.Date;
import java.util.LinkedList;

public class BlockORM {
    private int index;                          //区块链中区块的深度
    private long createTimestamp;               //创建时间戳
    private long createTimeUse;                 //创建用时
    private LinkedList<TxORM> data;             //块中包含的加密数据
    private String hash;                        //从块内容中获取的sha256哈希
    private String previousHash;                //对上一个块的哈希的引用
    private int hashOffset;                     //用来得到符合难度的 Hash的变量
    private int hashDifficulty;                 //Hash的难度

    public BlockORM(int index, long createTimestamp, long createTimeUse, LinkedList<TxORM> data, String hash, String previousHash, int hashOffset, int hashDifficulty) {
        this.index = index;
        this.createTimestamp = createTimestamp;
        this.createTimeUse = createTimeUse;
        this.data = data;
        this.hash = hash;
        this.previousHash = previousHash;
        this.hashOffset = hashOffset;
        this.hashDifficulty = hashDifficulty;
    }

    public BlockORM(int index, long createTimestamp, long createTimeUse, String hash, String previousHash, int hashOffset, int hashDifficulty) {
        this.index = index;
        this.createTimestamp = createTimestamp;
        this.createTimeUse = createTimeUse;
        this.hash = hash;
        this.previousHash = previousHash;
        this.hashOffset = hashOffset;
        this.hashDifficulty = hashDifficulty;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public long getCreateTimeUse() {
        return createTimeUse;
    }

    public void setCreateTimeUse(long createTimeUse) {
        this.createTimeUse = createTimeUse;
    }

    public LinkedList<TxORM> getData() {
        return data;
    }

    public void setData(LinkedList<TxORM> data) {
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public int getHashOffset() {
        return hashOffset;
    }

    public void setHashOffset(int hashOffset) {
        this.hashOffset = hashOffset;
    }

    public int getHashDifficulty() {
        return hashDifficulty;
    }

    public void setHashDifficulty(int hashDifficulty) {
        this.hashDifficulty = hashDifficulty;
    }
}

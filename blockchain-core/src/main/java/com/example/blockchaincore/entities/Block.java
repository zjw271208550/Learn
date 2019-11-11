package com.example.blockchaincore.entities;

import com.example.blockchaincore.funcations.HashFunction;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Block {
    private int index;              //区块链中区块的深度
    private long createTimestamp;   //创建时间戳
    private long createTimeUse;     //创建用时
    private String data;            //块中包含的加密数据
    private String hash;            //从块内容中获取的sha256哈希
    private String previousHash;    //对上一个块的哈希的引用
    private int hashOffset;         //用来得到符合难度的 Hash的变量
    private int hashDifficulty;     //Hash的难度

    public Block(int index, String data, String previousHash,int preHashDifficulty) {
        this.index = index;
        this.createTimestamp = generateCreateTimestamp();
        if(null == data)
            throw new RuntimeException("data is null");
        else
            this.data = data;
        if(null == previousHash)
            this.previousHash = "";
        else
            this.previousHash = previousHash;
        this.hashOffset = 0;
        this.hashDifficulty = preHashDifficulty;
        this.hash = HashFunction.generateHash(this);
        this.createTimeUse = generateCreateTimeUse(this.createTimestamp);
    }

    public int getIndex() {
        return index;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public String getData() {
        return data;
    }

    public String getHash() {
        return hash;
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

    public String getPreviousHash() {
        return previousHash;
    }

    public long getCreateTimeUse() {
        return createTimeUse;
    }

    private static long generateCreateTimestamp(){
        Date now = new Date();
        return now.getTime();
    }

    private static long generateCreateTimeUse(long createTimestamp){
        Date now = new Date();
        return now.getTime() - createTimestamp;
    }

}

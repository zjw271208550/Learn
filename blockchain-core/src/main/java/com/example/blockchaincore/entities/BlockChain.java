package com.example.blockchaincore.entities;

import com.example.blockchaincore.Consts;
import com.example.blockchaincore.funcations.HashFunction;

import java.util.LinkedList;
import java.util.List;

public class BlockChain {
    private LinkedList<Block> chain;
    private int lastIndex;
    private String lastHash;
    private int lastHashDifficulty;

    public BlockChain() {
        this.chain = new LinkedList<Block>();
        this.lastIndex = 0;
    }

    //自增时不适用，外来的加入使用
    public void addBlock(Block block){
        if(isLegalBlock(block)){
            chain.add(block);
            this.lastIndex = this.lastIndex+1;
            this.lastHash = block.getHash();
        }else
            throw new RuntimeException("block is illegal");
    }

    public void addBlock(String data){
        resetHashDifficulty();
        if(this.lastIndex==0) {
            Block block = new Block(lastIndex, data, lastHash, lastHashDifficulty);
            this.chain.add(block);
        }else {
            Block block = new Block(lastIndex + 1, data, lastHash,lastHashDifficulty);
            this.chain.add(block);
        }
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public String getLastHash(){
        return lastHash;
    }

    public LinkedList<Block> getChain() {
        return chain;
    }

    public int getLastHashDifficulty() {
        return lastHashDifficulty;
    }

    public long getCumuDifficulty(){
        long rel = 0L;
        for(Block b : this.chain){
            rel = rel + (long)Math.pow(2,b.getHashDifficulty());
        }
        return rel;
    }

    public void replaceChain(BlockChain otherChain){
        if (otherChain.getCumuDifficulty() > this.getCumuDifficulty())
            this.chain = otherChain.getChain();
    }

    private boolean isLegalBlock(Block block){
        Block lastBlock = this.chain.getLast();

        if(chain.isEmpty())
            return block.getIndex()==0;

        boolean rightPreHash =  lastBlock.getHash().equals(block.getPreviousHash());
        boolean rightHash = block.getHash().equals(HashFunction.generateHash(block));
        boolean rightIndex = lastBlock.getIndex()+1 == block.getIndex();
        return rightPreHash&&rightHash&&rightIndex;
    }

    private void resetHashDifficulty(){
        if(this.lastIndex == 0){
            this.lastHashDifficulty = Consts.HASH_DIFFICULTY_INIT;
        }else if(this.lastIndex % Consts.DIFFICULTY_RESET_AFTER_BLOCK==0) {
            List<Block> chainTmp =
                    this.chain.subList(this.lastIndex, this.lastIndex - (int) Consts.DIFFICULTY_RESET_AFTER_BLOCK);
            long cost = 0L;
            for (Block block : chainTmp)
                cost = cost + block.getCreateTimeUse();
            float rate = Consts.DIFFICULTY_RESET_AFTER_BLOCK * Consts.HASH_GENERATION_TIMEOUT_MS / cost;
            this.lastHashDifficulty = (int) (this.lastHashDifficulty * rate);
        }
    }
}

package com.example.blockchaincore.rdb.func;

import com.example.blockchaincore.Consts;
import com.example.blockchaincore.api.BlockData;
import com.example.blockchaincore.entities.*;
import com.example.blockchaincore.funcations.Crypt;
import com.example.blockchaincore.funcations.HashFunction;
import com.example.blockchaincore.funcations.Transaction;
import com.example.blockchaincore.rdb.mapper.BCMapper;
import com.example.blockchaincore.rdb.orm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ServiceFunction {
    private static final Logger logger = LoggerFactory.getLogger(ServiceFunction.class);

    /**
     * 检查数据是否被篡改
     * @param mapper 继承 BCMapper 的 Mapper
     * @param dataInDB
     * @return
     */
    public static boolean checkDataById(BCMapper mapper, BlockData dataInDB){
        UTxOutORM uTxOut = mapper.getUTxOutByDataId(dataInDB.getDataId());
        if(null == uTxOut){
            logger.error("该数据未在账本或未被挖掘");
            return false;
        }else {
            return uTxOut.getData().equals(dataInDB.getSHA256Data());
        }
    }

    public static boolean hasInit(BCMapper mapper){
        BlockORM initBlockInfo = mapper.getInitBlockInfo();
        if(null==initBlockInfo){
            return false;
        }

        Block initBlock = ORM2EntityFunction.BlockOrm2Entity(initBlockInfo);
        String initHash = HashFunction.generateHash(initBlock);
        if(initHash.equals(initBlockInfo.getHash())){
            return true;
        }else {
            System.out.println("创世块HASH不合法");
            return false;
        }
    }

    public static boolean initBlock(BCMapper mapper){
        Block initBlock = new Block(1,0,0,null,
                "","",0, Consts.HASH_DIFFICULTY_INIT);
        long start = new Date().getTime();
        String hash = HashFunction.generateHash(initBlock);
        long end = new Date().getTime();
        BlockORM initBlockORM = new BlockORM(1,0,end-start,hash,
                "",initBlock.getHashOffset(),Consts.HASH_DIFFICULTY_INIT);
        mapper.addOneBlock(initBlockORM);
        return true;
    }

    public static boolean addNewData(BCMapper mapper,String publicKey,String privateKey, BlockData data)
    throws Exception{
        TransactionIn txIn = new TransactionIn("",1,null);
        TransactionOut txOut = new TransactionOut(publicKey,data);
        TransactionGroup tx = new TransactionGroup(
                publicKey,
                Arrays.asList(new TransactionIn[] {txIn}),
                Arrays.asList(new TransactionOut[] {txOut})
        );
        String sign = Transaction.signForTransactionIn0(tx.getId(), publicKey, privateKey);
        txIn.setSignature(sign);
        mapper.addOneTx(
                new TxORM(null,publicKey,tx.getId()));
        mapper.addOneTxIn(
                new TxInORM(tx.getId(), txIn.getOutFromTxId(), txIn.getOutFromIndex(), txIn.getSignature()));
        mapper.addOneTxOut(
                new TxOutORM(tx.getId(),txOut.getToAddress(),txOut.getInfo().getDataId(),txOut.getInfo().getSHA256Data()));
        return true;
    }

    public static boolean mineTransactions(BCMapper mapper,String publicKey) throws Exception{
        List<TxORM> txOrms = mapper.getTxsByNull();
        if(null == txOrms || txOrms.size()==0){
            return true;
        }
        /*
         * 此处会广播 txOrms给全部节点，一起算
         */

        BlockORM lastBlockInfo = mapper.getLastBlockInfo();
        LinkedList<TransactionGroup> txs = new LinkedList<>();

        for(TxORM txORM: txOrms){
            TransactionGroup tx = ORM2EntityFunction.TxOrm2Entity(txORM);

            //正确的TxId
            if(tx.getId().equals(txORM.getId())){
                for(TransactionIn txIn:tx.getTxIns()){
                    boolean check = Crypt.verifySignature(
                            Crypt.base64String2Bytes(tx.getId()),
                            Crypt.base64String2Bytes(txORM.getFromAddress()),
                            Crypt.base64String2Bytes(txIn.getSignature())
                    );
                    if(!check){
                        throw new RuntimeException("交易ID"+tx.getId()+",签名不合法");
                    }
                }
                txs.add(tx);
            }else {
                throw new RuntimeException("交易ID:"+txORM.getId()+",不合法");
            }
        }

        Block newBlock = new Block(
                lastBlockInfo.getIndex()+1,
                txs,
                lastBlockInfo.getHash(),
                lastBlockInfo.getHashDifficulty());

        BlockORM newBlockOrm = new BlockORM(
                newBlock.getIndex(),
                newBlock.getCreateTimestamp(),
                newBlock.getCreateTimeUse(),
                null,
                newBlock.getHash(),
                newBlock.getPreviousHash(),
                newBlock.getHashOffset(),
                newBlock.getHashDifficulty());
        mapper.addOneBlock(newBlockOrm);

        for(TxORM txORM: txOrms){
            List<TxOutORM> txOutOrms = txORM.getTxOutORMS();
            mapper.updateTxBlockId(newBlockOrm.getIndex(),txORM.getId());
            int idx=1;
            for(TxOutORM txOutORM:txOutOrms){
                mapper.addOneUTxOut(
                        new UTxOutORM(publicKey,txOutORM.getDataId(),txOutORM.getData(),txORM.getId(),idx));
                idx += 1;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        String[] a = {"1","2"};
        List<String> b = Arrays.asList(new String[] {"1","2"});
        System.out.println(b);
    }
}

package com.example.blockchaincore.funcations;

import com.example.blockchaincore.api.BlockData;
import com.example.blockchaincore.entities.TransactionGroup;
import com.example.blockchaincore.entities.TransactionIn;
import com.example.blockchaincore.entities.TransactionOut;
import com.example.blockchaincore.entities.TransactionOutUnspent;
import javafx.scene.control.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

import static com.example.blockchaincore.funcations.Crypt.*;

public class Transaction {

    private static final Logger log = LoggerFactory.getLogger(Transaction.class);

    /**
     * 生成交易组的 ID
     */
    public static String generateId(TransactionGroup transaction){
        StringBuilder inId = new StringBuilder(transaction.getFromAddress());
        for(TransactionIn txIn:transaction.getTxIns()){
            inId.append(txIn.getOutFromTxId()+txIn.getOutFromIndex());
        }
        StringBuilder outId = new StringBuilder("");
        for(TransactionOut txOut:transaction.getTxOuts()){
            inId.append(txOut.getToAddress()+txOut.getInfo().getDataEncryptString());
        }
        return HashFunction.getSHA256Code(inId.toString()+outId.toString());
    }


    /**
     * 给一个 TxInORM 签名
     */
    public static String signForTransactionIn(String bodyId,
                                              TransactionIn txIn,
                                              String privateKey,
                                              List<TransactionOutUnspent> unspents)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
                    InvalidKeySpecException, InvalidKeyException, SignatureException {
       String idToSign = bodyId;

       TransactionOutUnspent referencedUnspentTxOut =
               findUnspentTxOut(txIn.getOutFromTxId(), txIn.getOutFromIndex(), unspents);
       if(null == referencedUnspentTxOut)
           throw new RuntimeException("没有匹配的未使用TxOut");
       String referencedAddress = referencedUnspentTxOut.getToAddress();

       if(!getPublicKey(privateKey).equals(referencedAddress))
           throw new RuntimeException("私钥生成的公钥与地址不符");

       String signature = bytes2Base64String(
               doSignature(
                       base64String2Bytes(idToSign),
                       base64String2Bytes(privateKey)
               )
       );

       return signature;
    }

    /**
     * 给一个 创始TxInORM 签名
     */
    public static String signForTransactionIn0(String bodyId,
                                                String addressTo,
                                                String privateKey)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException,
            InvalidKeySpecException, InvalidKeyException, SignatureException {
        String idToSign = bodyId;

        if(!getPublicKey(privateKey).equals(addressTo))
            throw new RuntimeException("私钥生成的公钥与地址不符");

        String signature = bytes2Base64String(
                doSignature(
                        base64String2Bytes(idToSign),
                        base64String2Bytes(privateKey)
                )
        );

        return signature;
    }

    /**
     * 根据私钥 Base64编码字符串获得 Base64编码后的公钥字符串
     */
    public static String getPublicKey(String privateKey)
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        byte[] privateK = base64String2Bytes(privateKey);
        byte[] publicK = getECPublicKeyFromPrivateKey(privateK);
        return bytes2Base64String(publicK);
    }


    /**
     * 判断是否是合法的交易组
     */
    public static boolean isLegalTransaction(TransactionGroup transaction,
                                             List<TransactionOutUnspent> unspents)
            throws IOException,NoSuchAlgorithmException,NoSuchProviderException,
                InvalidKeySpecException,InvalidKeyException,SignatureException{
        //判断 Id是否合法
        if(!generateId(transaction).equals(transaction.getId()) ){
            log.error("交易序列不合法：交易序列 ID不合法");
            return false;
        }


        //遍历所有的 txIn判断是否合法
        List<BlockData> txInInfos = new ArrayList<>();
        for(TransactionIn in:transaction.getTxIns()){
            //some of the txIns are invalid in t
            if(!isLegalTransactionIn(in,transaction,unspents)){
                log.error("交易序列不合法：发现签名不合法的 TxInORM，idx为{}",in.getOutFromIndex());
                return false;
            }
            txInInfos.add(getTransactionInInfo(in,unspents));
        }

        List<BlockData> txOutInfos = new ArrayList<>();
        for(TransactionOut out :transaction.getTxOuts()){
            txOutInfos.add(out.getInfo());
        }

        if(txInInfos.size()!=txOutInfos.size()) {
            log.error("交易输入 TxIn与交易输出 TxOut数量不符");
            return false;
        }

        for(BlockData txInInfo:txInInfos){
            int tmp=0;
            for(BlockData txOutInfo:txOutInfos) {
                if (txInInfo.equals(txOutInfo)){
                    break;
                }else {
                    tmp+=1;
                }
            }
            if(tmp==txOutInfos.size()) {
                return false;
            }
        }

        return true;
    }

    /**
     * 判断是否是合法的 txIn
     */
    private static boolean isLegalTransactionIn(TransactionIn txIn,
                                                TransactionGroup transaction,
                                                List<TransactionOutUnspent> unspents)
            throws IOException,NoSuchAlgorithmException,NoSuchProviderException,
                    InvalidKeySpecException,InvalidKeyException,SignatureException{
        //找到 txIn 引用的 txOutUnspent
        TransactionOutUnspent referencedUTxOut =
                findUnspentTxOut(txIn.getOutFromTxId(),txIn.getOutFromIndex(),unspents);

        //如果找不到说明 txIn 不合法
        if(null == referencedUTxOut) {
            log.error("交易输入 TxIn不合法：找不到引用的未使用交易输出 TxOutUnspent");
            return false;
        }

        String senderAddress = referencedUTxOut.getToAddress();

        //判断签名合法性
        return verifySignature(
                base64String2Bytes(transaction.getId()),
                base64String2Bytes(senderAddress),
                base64String2Bytes(txIn.getSignature()));
    }

    /**
     * 找到与交易输入匹配的未使用的交易输出
     */
    private static BlockData getTransactionInInfo(TransactionIn txIn,
                                                List<TransactionOutUnspent> unspents){
        TransactionOutUnspent referencedUTxOut =
                findUnspentTxOut(txIn.getOutFromTxId(),txIn.getOutFromIndex(),unspents);
        return referencedUTxOut.getInfo();
    }

    // public static boolean isLegalBlockTransaction(LinkedList<TransactionGroup> transactionBodies,
    //                                               List<TransactionOutUnspent> unspents,
    //                                               int blockIndex,
    //                                               BlockData baseData)throws Exception{
    //     //todo baseData 传入
    //     //验证块内第一条
    //     TransactionGroup body0 = transactionBodies.get(0);
    //     if(!isLegalBaseTransaction(body0, blockIndex, baseData)){
    //         return false;
    //     }
    //     LinkedList<TransactionIn> txIns = new LinkedList<>();
    //     for(TransactionGroup body:transactionBodies){
    //         txIns.addAll(body.getTxIns());
    //     }
    //     if (hasDuplicates(txIns)) {
    //         return false;
    //     }
    //     transactionBodies.poll();
    //     LinkedList<TransactionGroup> transactionBodiesNormal = transactionBodies;
    //     boolean rel = true;
    //     for(TransactionGroup body:transactionBodies){
    //         rel = rel && isLegalTransaction(body,unspents);
    //     }
    //     return rel;
    //
    // }
    //
    // private static boolean hasDuplicates(LinkedList<TransactionIn> txIns){
    //     LinkedList<String> outAddresses = new LinkedList<>();
    //     for(TransactionIn txIn:txIns){
    //         outAddresses.add(txIn.getOutFromTxId());
    //     }
    //     Set<String> outAddressesSet = new HashSet<>(outAddresses);
    //     return outAddresses.size()==outAddressesSet.size();
    // }
    //
    // private static boolean isLegalBaseTransaction(TransactionGroup body,
    //                                               int blockIndex,
    //                                               BlockData baseData){
    //     if(null==body)
    //         return false;
    //     if(!generateId(body).equals(body.getId()))
    //         return false;
    //     if(body.getTxIns().size() != 1)
    //         return false;
    //     if(body.getTxIns().get(0).getOutFromIndex() != blockIndex)
    //         return false;
    //     if(body.getTxOuts().size() != 1)
    //         return false;
    //     if(!body.getTxOuts().get(0).getInfo().equals(baseData))
    //         return false;
    //     return true;
    // }

    /**
     * 找到与 txOut 匹配的 txOutUnspent
     */
    private static TransactionOutUnspent findUnspentTxOut(String txInId,
                                                          int txInIndex,
                                                          List<TransactionOutUnspent> unspents){
        for(TransactionOutUnspent unspent: unspents){
            if(unspent.getOutFromTxId().equals(txInId) &&
                    unspent.getOutFromIndex() == txInIndex)
                return unspent;
        }
        return null;
    }

}

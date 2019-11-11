package com.example.blockchaincore.funcations;

import com.example.blockchaincore.api.BlockData;
import com.example.blockchaincore.entities.TransactionBody;
import com.example.blockchaincore.entities.TransactionIn;
import com.example.blockchaincore.entities.TransactionOut;
import com.example.blockchaincore.entities.TransactionOutUnspent;

import java.util.ArrayList;
import java.util.List;

public class Transaction {

    public static String generateId(TransactionBody transaction){
        StringBuilder inId = new StringBuilder("");
        for(TransactionIn txIn:transaction.getTxIns()){
            inId.append(txIn.getOutFromAddress()+txIn.getOutFromIndex());
        }
        StringBuilder outId = new StringBuilder("");
        for(TransactionOut txOut:transaction.getTxOuts()){
            inId.append(txOut.getSenderAddress()+txOut.getInfo());
        }
        return HashFunction.getSHA256Code(inId.toString()+outId.toString());
    }

    public static String signForTransactionIn(String bodyId,
                                              TransactionIn txIn,
                                              String privateKey,
                                              List<TransactionOutUnspent> unspents){
       String idToSign = bodyId;

       TransactionOutUnspent referencedUnspentTxOut =
               findUnspentTxOut(txIn.getOutFromAddress(), txIn.getOutFromIndex(), unspents);
       if(null == referencedUnspentTxOut)
           throw new RuntimeException("没有匹配的未使用TxOut");
       String referencedAddress = referencedUnspentTxOut.getSenderAddress();

       if(!getPublicKey(privateKey).equals(referencedAddress))
           throw new RuntimeException("私钥生成的公钥与地址不符");

       String key = ec.keyFromPrivate(privateKey, 'hex');
       String signature = toHexString(referencedAddress.sign(idToSign).toDER());

       return signature;
    }

    public static String getPublicKey(String privateKey){
        return ec.keyFromPrivate(privateKey, 'hex').getPublic().encode('hex');
    }

    private static TransactionOutUnspent findUnspentTxOut(String txOutId,
                                                          int txOutIndex,
                                                          List<TransactionOutUnspent> unspents){
        for(TransactionOutUnspent unspent: unspents){
            if(unspent.getOutFromAddress().equals(txOutId) &&
                    unspent.getOutFromIndex() == txOutIndex)
                return unspent;
        }
        return null;
    }

    public static boolean isLegalTransaction(TransactionBody transaction,
                                             List<TransactionOutUnspent> unspents){
        if(!generateId(transaction).equals(transaction.getId()) )
            return false;

        List<BlockData> txInInfos = new ArrayList<>();
        for(TransactionIn in:transaction.getTxIns()){
            //some of the txIns are invalid in t
            if(!isLegalTransactionIn(in,transaction,unspents)){
                return false;
            }
            txInInfos.add(getTransactionInInfo(in,unspents));
        }

        List<BlockData> txOutInfos = new ArrayList<>();
        for(TransactionOut out :transaction.getTxOuts()){
            txOutInfos.add(out.getInfo());
        }

        if(txInInfos.size()!=txOutInfos.size())
            return false;

        for(BlockData txInInfo:txInInfos){
            int tmp=0;
            for(BlockData txOutInfo:txOutInfos) {
                if (txInInfo.isEqualTo(txOutInfo)){
                    break;
                }else {
                    tmp+=1;
                }
            }
            if(tmp==txOutInfos.size())
                return false;
        }

        return true;
    }

    private static boolean isLegalTransactionIn(TransactionIn txIn,
                                                TransactionBody transaction,
                                                List<TransactionOutUnspent> unspents){
        TransactionOutUnspent referencedUTxOut =
                findUnspentTxOut(txIn.getOutFromAddress(),txIn.getOutFromIndex(),unspents);
        if(null == referencedUTxOut)
            return false;

        String senderAddress = referencedUTxOut.getSenderAddress();
        String key = ec.keyFromPublic(senderAddress, 'hex');
        return key.verify(transaction.getId(), txIn.getSignature());
    }

    private static BlockData getTransactionInInfo(TransactionIn txIn,
                                             List<TransactionOutUnspent> unspents){
        TransactionOutUnspent referencedUTxOut =
                findUnspentTxOut(txIn.getOutFromAddress(),txIn.getOutFromIndex(),unspents);
        return referencedUTxOut.getInfo();
    }

}

package com.example.blockchaincore.rdb.func;

import com.example.blockchaincore.api.BlockData;
import com.example.blockchaincore.entities.Block;
import com.example.blockchaincore.entities.TransactionGroup;
import com.example.blockchaincore.entities.TransactionIn;
import com.example.blockchaincore.entities.TransactionOut;
import com.example.blockchaincore.rdb.orm.BlockORM;
import com.example.blockchaincore.rdb.orm.TxInORM;
import com.example.blockchaincore.rdb.orm.TxORM;
import com.example.blockchaincore.rdb.orm.TxOutORM;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ORM2EntityFunction {

    public static Block BlockOrm2Entity(BlockORM orm){
        LinkedList<TransactionGroup> txList = new LinkedList<>();
        orm.getData().forEach(txORM -> txList.add(TxOrm2Entity(txORM)));
        return new Block(
                orm.getIndex(),
                orm.getCreateTimestamp(),
                orm.getCreateTimeUse(),
                txList,
                orm.getHash(),
                orm.getPreviousHash(),
                orm.getHashOffset(),
                orm.getHashDifficulty()
                );
    }

    public static TransactionGroup TxOrm2Entity(TxORM orm){
        List<TransactionIn> txIns = new ArrayList<>();
        orm.getTxInORMS().forEach(txInORM -> txIns.add(TxInOrm2Entity(txInORM)));
        List<TransactionOut> txOuts = new ArrayList<>();
        orm.getTxOutORMS().forEach(txOutORM -> txOuts.add(TxOutOrm2Entity(txOutORM)));
        return new TransactionGroup(
                orm.getId(),
                orm.getFromAddress(),
                txIns,
                txOuts
        );
    }

    public static TransactionIn TxInOrm2Entity(TxInORM orm){
        return new TransactionIn(orm.getPreTxId(),orm.getPreTxIdx(),orm.getSign());
    }

    public static TransactionOut TxOutOrm2Entity(TxOutORM orm){
        return new TransactionOut(orm.getAddressTo(), new BlockData(orm.getDataId(),orm.getData()) {});
    }
}

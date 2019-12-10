package com.example.blockchaincore.rdb.mapper;

import com.example.blockchaincore.rdb.orm.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface BCMapper {

    @Select("SELECT n_index,dt_create,n_use_time,c_hash,c_prehash,n_offset,n_difficaulty " +
            "FROM db_data.t_blockchain " +
            "WHERE n_index=#{index}")
    @Results({
            @Result(column="n_index",property="index"),
            @Result(column="dt_create",property="createTimestamp"),
            @Result(column="n_use_time",property="createTimeUse"),
            @Result(column="n_index",property="data",
                many = @Many(
                        select = "com.example.blockchaincore.rdb.mapper.BCMapper.getTxsByIndex",
                        fetchType = FetchType.LAZY
                )
            ),
            @Result(column="c_hash",property="hash"),
            @Result(column="c_prehash",property="previousHash"),
            @Result(column="n_offset",property="hashOffset"),
            @Result(column="n_difficaulty",property="hashDifficulty")
    })
    public BlockORM getBlockByIndex(@Param("index") int index);

    @Select("SELECT n_block_index,c_id,c_from_addr " +
            "FROM db_data.t_txs " +
            "WHERE n_block_index=#{n_index}")
    @Results({
            @Result(column="n_block_index",property="blockIdx"),
            @Result(column="c_id",property="id"),
            @Result(column="c_from_addr",property="fromAddress"),
            @Result(column="c_id",property="txInORMS",
                    many = @Many(
                            select = "com.example.blockchaincore.rdb.mapper.BCMapper.getTxInsById",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(column="c_id",property="txOutORMS",
                    many = @Many(
                            select = "com.example.blockchaincore.rdb.mapper.BCMapper.getTxOutsById",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    public List<TxORM> getTxsByIndex(@Param("n_index") int n_index);

    @Select("SELECT n_block_index,c_id,c_from_addr " +
            "FROM db_data.t_txs " +
            "WHERE n_block_index ISNULL")
    @Results({
            @Result(column="n_block_index",property="blockIdx"),
            @Result(column="c_id",property="id"),
            @Result(column="c_from_addr",property="fromAddress"),
            @Result(column="c_id",property="txInORMS",
                    many = @Many(
                            select = "com.example.blockchaincore.rdb.mapper.BCMapper.getTxInsById",
                            fetchType = FetchType.LAZY
                    )
            ),
            @Result(column="c_id",property="txOutORMS",
                    many = @Many(
                            select = "com.example.blockchaincore.rdb.mapper.BCMapper.getTxOutsById",
                            fetchType = FetchType.LAZY
                    )
            )
    })
    public List<TxORM> getTxsByNull();

    @Select("SELECT c_tx_id,c_pretx_id,n_pretx_idx,c_sign " +
            "FROM db_data.t_txins " +
            "WHERE c_tx_id=#{c_id}")
    @Results({
            @Result(column="c_tx_id",property="txId"),
            @Result(column="c_pretx_id",property="preTxId"),
            @Result(column="n_pretx_idx",property="preTxIdx"),
            @Result(column="c_sign",property="sign")
    })
    public List<TxInORM> getTxInsById(@Param("c_id") String c_id);

    @Select("SELECT c_tx_id,c_address_to,c_data_id,c_data " +
            "FROM db_data.t_txouts " +
            "WHERE c_tx_id=#{c_id}")
    @Results({
            @Result(column="c_tx_id",property="txId"),
            @Result(column="c_address_to",property="addressTo"),
            @Result(column="c_data_id",property="dataId"),
            @Result(column="c_data",property="data")
    })
    public List<TxOutORM> getTxOutsById(@Param("c_id") String c_id);



    @Insert("INSERT INTO db_data.t_blockchain " +
            "(n_index,dt_create,n_use_time,c_hash,c_prehash,n_offset,n_difficaulty) " +
            "VALUES(#{index},#{createTimestamp},#{createTimeUse}," +
            "#{hash},#{previousHash},#{hashOffset},#{hashDifficulty})")
    public void addOneBlock(BlockORM blockORM);

    @Insert("INSERT INTO db_data.t_txs " +
            "(n_block_index,c_id,c_from_addr) " +
            "VALUES(#{blockIdx},#{id},#{fromAddress})")
    public void addOneTx(TxORM txORM);

    @Update("UPDATE db_data.t_txs SET n_block_index=#{bid} WHERE c_id=#{cid}")
    public void updateTxBlockId(@Param("bid") int bid,@Param("cid") String cid);

    @Insert("INSERT INTO db_data.t_txins " +
            "(c_tx_id,c_pretx_id,n_pretx_idx,c_sign) " +
            "VALUES(#{txId},#{preTxId},#{preTxIdx},#{sign})")
    public void addOneTxIn(TxInORM txInORM);

    @Insert("INSERT INTO db_data.t_txouts " +
            "(c_tx_id,c_address_to,c_data_id,c_data) " +
            "VALUES(#{txId},#{addressTo},#{dataId},#{data})")
    public void addOneTxOut( TxOutORM txOutORM);


    @Select("SELECT c_publickey,c_data_id,c_data,c_txid,n_txidx " +
            "FROM db_data.t_utxos " +
            "WHERE c_publickey=#{publicKey}")
    @Results({
            @Result(column="c_publickey",property="publicKey"),
            @Result(column="c_data_id",property="dataId"),
            @Result(column="c_data",property="data"),
            @Result(column="c_txid",property="txId"),
            @Result(column="n_txidx",property="txIdx")
    })
    public List<UTxOutORM> getUTxOutsByPublicKey(@Param("publicKey") String publicKey);

    @Select("SELECT c_publickey,c_data_id,c_data,c_txid,n_txidx " +
            "FROM db_data.t_utxos " +
            "WHERE c_txid=#{txId}")
    @Results({
            @Result(column="c_publickey",property="publicKey"),
            @Result(column="c_data_id",property="dataId"),
            @Result(column="c_data",property="data"),
            @Result(column="c_txid",property="txId"),
            @Result(column="n_txidx",property="txIdx")
    })
    public List<UTxOutORM> getUTxOutsByTxId(@Param("txId") String txId);

    @Select("SELECT c_publickey,c_data_id,c_data,c_txid,n_txidx " +
            "FROM db_data.t_utxos " +
            "WHERE c_txid=#{txId} AND n_txidx=#{txIds}")
    @Results({
            @Result(column="c_publickey",property="publicKey"),
            @Result(column="c_data_id",property="dataId"),
            @Result(column="c_data",property="data"),
            @Result(column="c_txid",property="txId"),
            @Result(column="n_txidx",property="txIdx")
    })
    public UTxOutORM getUTxOutByTx(@Param("txId") String txId,@Param("txIds") int txIds);

    @Select("SELECT c_publickey,c_data_id,c_data,c_txid,n_txidx " +
            "FROM db_data.t_utxos " +
            "WHERE c_data_id=#{dataId}")
    @Results({
            @Result(column="c_publickey",property="publicKey"),
            @Result(column="c_data_id",property="dataId"),
            @Result(column="c_data",property="data"),
            @Result(column="c_txid",property="txId"),
            @Result(column="n_txidx",property="txIdx")
    })
    public UTxOutORM getUTxOutByDataId(@Param("dataId") String dataId);


    @Insert("INSERT INTO db_data.t_utxos " +
            "(c_publickey,c_data_id,c_data,c_txid,n_txidx) " +
            "VALUES(#{publicKey},#{dataId},#{data},#{txId},#{txIdx})")
    public void addOneUTxOut( UTxOutORM uTxOutORM);

    @Delete("DELETE FROM db_data.t_utxos " +
            "WHERE c_data_id=#{dataId}")
    public void delOneUTxOutById(@Param("dataId") String dataId);


    @Select("SELECT n_index,dt_create,n_use_time,c_hash,c_prehash,n_offset,n_difficaulty " +
            "FROM db_data.t_blockchain " +
            "ORDER BY n_index DESC LIMIT 1")
    @Results({
            @Result(column="n_index",property="index"),
            @Result(column="dt_create",property="createTimestamp"),
            @Result(column="n_use_time",property="createTimeUse"),
            @Result(column="c_hash",property="hash"),
            @Result(column="c_prehash",property="previousHash"),
            @Result(column="n_offset",property="hashOffset"),
            @Result(column="n_difficaulty",property="hashDifficulty")
    })
    public BlockORM getLastBlockInfo();


    @Select("SELECT n_index,dt_create,n_use_time,c_hash,c_prehash,n_offset,n_difficaulty " +
            "FROM db_data.t_blockchain " +
            "WHERE n_index=1")
    @Results({
            @Result(column="n_index",property="index"),
            @Result(column="dt_create",property="createTimestamp"),
            @Result(column="n_use_time",property="createTimeUse"),
            @Result(column="c_hash",property="hash"),
            @Result(column="c_prehash",property="previousHash"),
            @Result(column="n_offset",property="hashOffset"),
            @Result(column="n_difficaulty",property="hashDifficulty")
    })
    public BlockORM getInitBlockInfo();
}

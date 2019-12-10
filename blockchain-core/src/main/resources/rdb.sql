CREATE SCHEMA IF NOT EXISTS db_data;

DROP TABLE IF EXISTS db_data.t_blockchain CASCADE;
CREATE TABLE db_data.t_blockchain(
	n_index INT8,
	dt_create INT8,
	n_use_time INT8,
	c_hash TEXT,
	c_prehash TEXT,
	n_offset INT8,
	n_difficaulty INT
);
ALTER TABLE db_data.t_blockchain
ADD CONSTRAINT pk_t_blockchain_index PRIMARY KEY (n_index);

DROP TABLE IF EXISTS db_data.t_txs CASCADE;
CREATE TABLE db_data.t_txs(
	n_block_index INT8,
	c_id TEXT,
	c_from_addr TEXT
);
ALTER TABLE db_data.t_txs
ADD CONSTRAINT pk_t_txs_index PRIMARY KEY (c_id);
ALTER TABLE db_data.t_txs
ADD CONSTRAINT fk_t_txs_block_index FOREIGN KEY (n_block_index)
REFERENCES db_data.t_blockchain(n_index);

DROP TABLE IF EXISTS db_data.t_txins CASCADE;
CREATE TABLE db_data.t_txins(
	c_tx_id TEXT,
	c_pretx_id TEXT,
	n_pretx_idx INT,
	c_sign TEXT
);
ALTER TABLE db_data.t_txins
ADD CONSTRAINT fk_t_txins_tx_id FOREIGN KEY (c_tx_id)
REFERENCES db_data.t_txs(c_id);

DROP TABLE IF EXISTS db_data.t_txouts CASCADE;
CREATE TABLE db_data.t_txouts(
	c_tx_id TEXT,
	c_address_to TEXT,
	c_data_id VARCHAR,
	c_data TEXT
);
ALTER TABLE db_data.t_txouts
ADD CONSTRAINT fk_t_txouts_tx_id FOREIGN KEY (c_tx_id)
REFERENCES db_data.t_txs(c_id);

DROP TABLE IF EXISTS db_data.t_utxos CASCADE;
CREATE TABLE t_utxos(
	c_publickey TEXT,
	c_data TEXT,
	c_data_id VARCHAR,
	c_txid TEXT,
	n_txidx INT
);
ALTER TABLE db_data.t_utxos
ADD CONSTRAINT pk_t_utxos_data_id PRIMARY KEY (c_data_id);
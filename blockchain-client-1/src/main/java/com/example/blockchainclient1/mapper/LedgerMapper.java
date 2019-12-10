package com.example.blockchainclient1.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class LedgerMapper {
    private final Logger logger = LoggerFactory.getLogger(LedgerMapper.class);
    private JedisPool pool = null;

    public LedgerMapper() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(200);
        config.setMaxIdle(200);
        config.setMinIdle(10);
        config.setMaxWaitMillis(30000);
        pool = new JedisPool(config, "172.16.32.38", 6379, 2000, "123456", 0);
    }

    private Jedis getConn() {
        Jedis jedis = null;
        jedis = pool.getResource();
        return jedis;
    }

    private  void closePool() {
        if(pool != null){
            pool.destroy();
        }
    }

    private static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public void setHash(String key, String field, String value){
        Jedis jedis = this.getConn();
        try {
            jedis.hset(key, field, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            this.close(jedis);
        }
    }

    public String getHash(String key, String field){
        Jedis jedis = this.getConn();
        try {
            return jedis.hget(key, field);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}

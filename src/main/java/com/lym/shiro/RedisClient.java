package com.lym.shiro;

import java.util.Set;

import org.crazycake.shiro.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient extends RedisManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private static JedisPool jedisPool = null;

    public RedisClient(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void init() {
        super.init();
    }

    @Override
    public byte[] get(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        byte[] value;

        try {
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("redis key: " + new String(key) + " get value occur exception");
            throw new RuntimeException("redis operation error:", e);
        } finally {
            jedis.close();
        }

        return value;
    }

    @Override
    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.set(key, value);
            Integer expire = getExpire();
            if(expire != 0) {
                jedis.expire(key, expire);
            }
        } catch (Exception e) {
            logger.error("redis key: " + new String(key) + " set value: " + new String(value) + " occur exception");
            throw new RuntimeException("redis operation error:", e);
        } finally {
            jedis.close();
        }

        return value;
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.set(key, value);
            if(expire != 0) {
                jedis.expire(key, expire);
            }
        } catch (Exception e) {
            logger.error("redis key: " + new String(key) + " set value: " + new String(value) + " in expire: " + String.valueOf(expire) + " occur exception");
            throw new RuntimeException("redis operation error:", e);
        } finally {
            jedis.close();
        }

        return value;
    }

    @Override
    public void del(byte[] key) {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.del(key);
        } catch (Exception e) {
            logger.error("redis key: " + new String(key) + " del value occur exception");
            throw new RuntimeException("redis operation error:", e);
        } finally {
            jedis.close();
        }
    }

    @Override
    public void flushDB() {
        Jedis jedis = jedisPool.getResource();

        try {
            jedis.flushDB();
        } catch (Exception e) {
            logger.error("redis flushDB occur exception");
            throw new RuntimeException("redis operation error:", e);
        } finally {
            jedis.close();
        }

    }

    @Override
    public Long dbSize() {
        Long dbSize = Long.valueOf(0L);
        Jedis jedis = jedisPool.getResource();

        try {
            dbSize = jedis.dbSize();
        } catch (Exception e) {
            logger.error("redis get dbSize occur exception");
            throw new RuntimeException("redis operation error:", e);
        } finally {
            jedis.close();
        }

        return dbSize;
    }

    @Override
    public Set<byte[]> keys(String pattern) {
        Set keys = null;
        Jedis jedis = jedisPool.getResource();

        try {
            keys = jedis.keys(pattern.getBytes());
        } catch (Exception e) {
            logger.error("redis get keys in pattern: " + pattern + " occur exception");
            throw new RuntimeException("redis operation error:", e);
        } finally {
            jedis.close();
        }
        
        return keys;
    }
    
}
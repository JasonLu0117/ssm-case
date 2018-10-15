package com.lym.shiro;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.lym.util.ByteObjectUtil;

@Component
public class RedisUtil {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds
     */
    public void expire(String key, int seconds) {
        if (seconds <= 0) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        jedis.expire(key, seconds);
        jedis.close();
    }

    public void expire(byte[] key, int seconds) {
        if (seconds <= 0) {
            return;
        }
        Jedis jedis = jedisPool.getResource();
        jedis.expire(key, seconds);
        jedis.close();
    }
    
    /* 判断key是否存在 */
    public boolean isExist(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean exists = jedis.exists(key);
        return exists;
    }
    
    /* 字符串类型操作 */
    /* 获取字符串数据集 */
    public String getStr(String key) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }

    /* 新增字符串数据集 */
    public void addStr(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        jedis.set(key, value);
        jedis.close();
    }
    
    /* 删除字符串数据集 */
    public void delStr(String key) {
        Jedis jedis = jedisPool.getResource();
        jedis.del(key);
        jedis.close();
    }
    
    // 保存byte类型数据
    public void addByte(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        String result = "";
        try {
            if (!jedis.exists(key)) {
                jedis.set(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    // 获取byte类型数据
    public byte[] getByte(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        byte[] bytes = null;
        try {
            bytes = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return bytes;
    }

    /* list类型操作 */
    /* 获取list数据集 */
    public List<String> getList(String key, Long start, Long end) {
        Jedis jedis = jedisPool.getResource();
        List<String> value = jedis.lrange(key, start, end);
        jedis.close();
        return value;
    }

    /* 新增list数据集 */
    public void addList(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        jedis.lpush(key, value);
        jedis.close();
    }

    /* set类型操作 */
    /* 获取set数据集 */
    public Set<String> getSet(String key) {
        Jedis jedis = jedisPool.getResource();
        Set<String> value = jedis.smembers(key);
        jedis.close();
        return value;
    }
    
    /* 模糊查询key的set集 */
    public Set<String> getKeySet(String key) {
        Jedis jedis = jedisPool.getResource();
        Set<String> value = jedis.keys(key);
        jedis.close();
        return value;
    }

    /* 新增set数据集 */
    public void addSet(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        jedis.sadd(key, value);
        jedis.close();
    }

    /* map类型操作 */
    /* 获取map数据集 */
    public Map<String, String> getMap(String key) {
        Jedis jedis = jedisPool.getResource();
        Iterator<String> iter = jedis.hkeys(key).iterator();
        Map<String, String> map = new HashMap<>();
        while (iter.hasNext()) {
            String name = iter.next();
            List<String> value = jedis.hmget(key, name);
            map.put(name, value.get(0));
        }
        jedis.close();
        return map;
    }

    /* 获取map单个对象 */
    public String getMapOne(String key, String keymap) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.hget(key, keymap);
        jedis.close();
        return value;
    }

    /* 新增map数据集 */
    public void addMap(String key, String keyMap, String keyValue) {
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.hset(key, keyMap, keyValue);
            jedis.close();
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
    }

    /* 删除单个map数据 */
    public void delMap(String key, String keymap) {
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.hdel(key, keymap);
            jedis.close();
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
    }

    /* 修改map数据集 */
    public void setMap(String key, String keyMap, String keyValue) {
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.hset(key, keyMap, keyValue);
            jedis.close();
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
    }
    
    public void addSortSet(String key, double score, String member) {
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.zadd(key, score, member);
            jedis.close();
        } catch (Exception e) {
            //System.out.println(e.toString());
        }
    }
    
    public Set<String> getSortSet(String key) {
        Jedis jedis = jedisPool.getResource();
        Set<String> set = jedis.zrevrange(key, 0, -1);
        jedis.close();
        return set;
    }
    
    /* 序列化map类型操作 */
    public <T> void setSerialMap(String key, Map<String, T> map) {
        try {
            Jedis jedis = jedisPool.getResource();
            jedis.set(key.getBytes(), ByteObjectUtil.serialize(map));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> Map<String, T> getSerialMap(String key) {
        Jedis jedis = jedisPool.getResource();
        byte[] in = jedis.get(key.getBytes());
        Map<String, T> map = (Map<String, T>) ByteObjectUtil.deserialize(in);
        return map;
    }
    
}
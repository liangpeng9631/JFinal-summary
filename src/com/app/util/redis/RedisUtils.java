package com.app.util.redis;

import com.jfinal.json.FastJson;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import redis.clients.jedis.Jedis;

public class RedisUtils {

    public static String setToJson(String key, Object object) {
        Jedis jedis = null;
        Cache cache = null;
        try
        {
            cache = Redis.use("redis_00");
            jedis = cache.getJedis();
            String value = FastJson.getJson().toJson(object);
            return jedis.set(key, value);
        }
        finally
        {
            cache.close(jedis);
        }
    }
    
    public static String set(String key, String value) {
 Jedis jedis = null;
        Cache cache = null;
        try
        {
            cache = Redis.use("redis_00");
            jedis = cache.getJedis();
            return jedis.set(key, value);
        }
        finally
        {
            cache.close(jedis);
        }
    }

    public static String setLong(String key, Long value) {
        return set(key, String.valueOf(value));
    } 

    public static String get(String key) {
        Jedis jedis = null;
        Cache cache = null;
        try
        {
            cache = Redis.use("redis_00");
            jedis = cache.getJedis();
            return jedis.get(key);
        }
        finally
        {
            cache.close(jedis);
        }
    }
    
    public static long expire(String key, int seconds)
    {
        Jedis jedis = null;
        Cache cache = null;
        try
        {
            cache = Redis.use("redis_00");
            jedis = cache.getJedis();
            return jedis.expire(key, seconds);
        }
        finally
        {
            cache.close(jedis);
        }
    }
}

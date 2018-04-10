package com.xyw.spbodemo.util;

import com.alibaba.fastjson.JSON;
import com.xyw.spbodemo.controller.MessageController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

import static com.sun.scenario.Settings.set;

@Service
public class JedisAdapter implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private Jedis jedis = null;
    private static JedisPool pool = null;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d : %s ", index, obj.toString()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("localhost", 6379);
    }

    private Jedis getJedis() {
        //return jedis
        return pool.getResource();
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } catch (Exception e) {
            logger.error("happen exception : " + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("happen exception:" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("the exception happeded:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("the exception happeded:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error("the exception happeded:" + e.getMessage());
            return false;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("the exception happeded:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public void setObject(String key, Object object) {
        set(key, JSON.toJSONString(object));
    }

    //input the block queue
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error("the exception happeded:" + e.getMessage());
            return 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    //get the event from the block queue
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {

            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("happen exception:" + e.getMessage());
            return null;
        }finally {
            if(jedis!=null){
                jedis.close();
            }
        }

    }

}

package cn.tellsea.service.impl;

import cn.tellsea.dto.RedisInfo;
import cn.tellsea.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author tycoding
 * @date 2019-02-25
 */
@Service
public class RedisServiceImpl implements RedisService {

    private static String redisCode = "utf-8";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private RedisConnection execute() {
        return (RedisConnection) redisTemplate.execute((RedisCallback) redisConnection -> redisConnection);
    }
    /**
     * 获取redis基础info 数据
     * @return java.util.List<cn.tellsea.dto.RedisInfo>
     * @create 2019/9/10 14:09
     */
    @Override
    public List<RedisInfo> getRedisInfo() {
        try {
            List<RedisInfo> list = new ArrayList<>();
            Properties info = execute().info();
            for (String key : info.stringPropertyNames()) {
                RedisInfo redisInfo = new RedisInfo();
                redisInfo.setKey(key);
                redisInfo.setValue(info.getProperty(key));
                list.add(redisInfo);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 获取redis内存信息
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @create 2019/9/10 13:53
     */
    @Override
    public Map<String, Object> getRedisMemory() {
        return getData("memory", execute().info("memory").get("used_memory"));
    }
    /**
     * 获取redis key的数量
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @create 2019/9/10 13:53
     */
    @Override
    public Map<String, Object> getRedisDbSize() {
        return getData("dbsize", execute().dbSize());
    }
    /**
     * 通过正则表达式查询keys列表
     * @param pattern
     * @return java.util.Set<java.lang.String>
     * @create 2019/9/10 13:54
     */
    @Override
    public Set<String> getKeys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }
    /**
     * 通过key获取value值
     * @param key
     * @return java.lang.String
     * @create 2019/9/10 13:54
     */
    @Override
    public String get(String key) {
        try {
            byte[] bytes = execute().get(key.getBytes());
            if (bytes != null) {
                return new String(bytes, redisCode);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 添加key-value键值对数据
     * @param key
     * @param value
     * @return java.lang.Boolean
     * @create 2019/9/10 13:55
     */
    @Override
    public Boolean set(String key, String value) {
        return execute().set(key.getBytes(), value.getBytes());
    }
    /**
     * 删除key
     * @param keys
     * @return java.lang.Long
     * @create 2019/9/10 14:06
     */
    @Override
    public Long del(String... keys) {
        long result = 0;
        for (int i = 0; i < keys.length; i++) {
            result += execute().del(keys[i].getBytes());
        }
        return result;
    }
    /**
     * 判断key是否存在
     * @param keys
     * @return java.lang.Long
     * @create 2019/9/10 14:06
     */
    @Override
    public Long exists(String... keys) {
        long result = 0;
        for (int i = 0; i < keys.length; i++) {
            if (execute().exists(keys[i].getBytes())) {
                result++;
            }
        }
        return result;
    }
    /**
     * 获取key剩余时间
     * @param key
     * @return java.lang.Long
     * @create 2019/9/10 14:07
     */
    @Override
    public Long pttl(String key) {
        return execute().pTtl(key.getBytes());
    }
    /**
     * 以毫秒为单位设置key的生成时间
     * @param key
     * @param time
     * @return java.lang.Long
     * @create 2019/9/10 14:07
     */
    @Override
    public Long pexpire(String key, Long time) {
        if (execute().pExpire(key.getBytes(), time)) {
            return 1L;
        }
        return 0L;
    }

    private Map<String, Object> getData(String name, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("create_time", (new Date()).getTime());
        map.put(name, data);
        return map;
    }
}

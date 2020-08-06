package com.hezhan.mybatis.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.hezhan.mybatis.demo.exception.RemoteException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis操作工具类
 */
@Component
@Transactional
public class RedisUtil {

    @Autowired
    private StringRedisTemplate template;

    @Value("${redis.default.timeout}")
    private int timeout;

    /**
     * 批量保存数据到缓存中，并且没有设置过期时间
     *
     * @param cacheMap 要保存的缓存数据
     */
    public void mSet(Map<String, String> cacheMap) {
        ValueOperations<String, String> ops = template.opsForValue();
        ops.multiSet(cacheMap);
    }

    /**
     * 批量保存数据到缓存中，且设置过期时间
     *
     * @param cacheMap 要保存的缓存数据
     * @param timeout  过期时间，以秒为单位
     */
    public void mSet(Map<String, String> cacheMap, int timeout) {
        ValueOperations<String, String> ops = template.opsForValue();
        ops.multiSet(cacheMap);
        for (Map.Entry<String, String> entry : cacheMap.entrySet()) {
            template.expire(entry.getKey(), timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 根据key获取对应的缓存数据
     *
     * @param key 缓存key
     * @return
     */
    public String get(String key) {
        ValueOperations<String, String> ops = template.opsForValue();
        return ops.get(key);
    }

    /**
     * 根据key获取对应的缓存数据，并返回指定的类型
     *
     * @param key   缓存key
     * @param clazz 指定的类型
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {

        ValueOperations<String, String> ops = template.opsForValue();
        String value = ops.get(key);
        return JSONObject.parseObject(value, clazz);
    }

    /**
     * 保存单个数据到缓存中，设置默认时间，即从配置文件中获取的时间
     *
     * @param key   缓存key
     * @param value 要保存的数据
     */
    public void set(String key, String value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 保存单个数据到缓存中，设置过期时间
     *
     * @param key     缓存key
     * @param value   要保存的数据
     * @param timeOut 过期时间，以秒为单位
     */
    public void set(String key, String value, int timeOut) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return;
        }
        ValueOperations<String, String> ops = template.opsForValue();
        ops.set(key, value, timeOut, TimeUnit.SECONDS);
    }

    /**
     * 根据key从缓存中删除单个数据
     *
     * @param key 缓存key
     * @return
     */
    public boolean delete(String key) {
        boolean deleteSuccess = true;
        if (StringUtils.isEmpty(key)) {
            return true;
        }
        try {
            deleteSuccess = template.delete(key);
        } catch (Exception e) {
            throw new RemoteException("删除缓存key为" + key + "时出现错误，错误原因为：", e);
        }
        return deleteSuccess;
    }

    /**
     * 根据传入的key的集合，批量删除缓存数据
     *
     * @param keys key集合
     */
    public void delete(List<String> keys) {
        template.delete(keys);
    }
}

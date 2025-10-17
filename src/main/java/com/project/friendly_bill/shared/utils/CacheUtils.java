package com.project.friendly_bill.shared.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CacheUtils {
    final RedisTemplate<Object, Object> redisTemplate;

    public void putToCache(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void putToCache(String key, Object value, Long ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public Object getFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void removeFromCache(String key) {
        redisTemplate.delete(key);
    }

    public boolean existsInCache(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}

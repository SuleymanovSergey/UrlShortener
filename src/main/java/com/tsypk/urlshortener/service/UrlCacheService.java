package com.tsypk.urlshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UrlCacheService {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public UrlCacheService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void cacheUrl(String shortCode, String originalUrl) {
        redisTemplate.opsForValue().set(shortCode, originalUrl);
    }

    public String getOriginalUrl(String shortCode) {
        return redisTemplate.opsForValue().get(shortCode);
    }
}


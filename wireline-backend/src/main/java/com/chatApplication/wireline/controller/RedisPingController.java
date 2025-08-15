package com.chatApplication.wireline.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class RedisPingController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/redis")
    public ResponseEntity<String> testRedis() {
        try {
            redisTemplate.opsForValue().set("test-key", "test-value");
            String value = redisTemplate.opsForValue().get("test-key");
            return ResponseEntity.ok("✅ Redis connected, value: " + value);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Redis connection failed: " + e.getMessage());
        }
    }
}

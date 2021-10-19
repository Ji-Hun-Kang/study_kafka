package com.jikang.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisComponent implements ApplicationRunner {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ValueOperations<String, String> data = this.redisTemplate.opsForValue();
        data.set("001", "hello");
        data.set("002", "redis");
        data.set("003", "start");

        String firstValue = data.get("001");
        String secoundValue = data.get("002");
        String thirdValue = data.get("003");

        System.out.println("first value : " + firstValue);
        System.out.println("secound value : " + secoundValue);
        System.out.println("third value : " + thirdValue);
    }
}

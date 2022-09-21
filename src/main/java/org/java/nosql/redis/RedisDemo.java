package org.java.nosql.redis;

import org.java.nosql.Application;
//错误导包
//import org.junit.jupiter.api.Test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * 这是一个测试redis分布式锁的demo
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisDemo {

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testRedisDemo() throws  InterruptedException{
        RLock lock = redissonClient.getLock("anyLock12");
        boolean isLock = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (isLock) {
            try {
                System.out.println("hello,this is redis lock");
                lock.unlock();
            }finally {
                lock.unlock();
            }
        }
//        System.out.println("这是一个测试用例");

    }
}

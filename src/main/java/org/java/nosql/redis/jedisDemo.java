package org.java.nosql.redis;

import redis.clients.jedis.Jedis;

import java.util.Set;

public class jedisDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("123456");
        Set<String> keys = jedis.keys("*");
        for (String ke :
                keys) {
            System.out.println(ke);
        }
        jedis.close();
    }
}

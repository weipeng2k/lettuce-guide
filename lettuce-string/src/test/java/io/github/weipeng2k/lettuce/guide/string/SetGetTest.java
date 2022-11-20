package io.github.weipeng2k.lettuce.guide.string;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author weipeng2k 2022-11-20 22:05:20
 */
public class SetGetTest {
    private static RedisCommands<String, String> redisCommands;

    @BeforeClass
    public static void init() {
        RedisURI redisURI = RedisURI.builder()
                .withHost("weipeng2k-workstation")
                .withPort(6379)
                .withDatabase(0)
                .build();

        redisCommands = RedisClient.create(redisURI).connect().sync();
    }

    @Before
    public void setKey() {
        redisCommands.set("z", "z");
    }

    @Test
    public void set() {
        String set = redisCommands.set("z", "z");
        Assert.assertEquals("OK", set);
    }

    @Test
    public void get() {
        String z = redisCommands.get("z");
        Assert.assertEquals("z", z);
    }
}

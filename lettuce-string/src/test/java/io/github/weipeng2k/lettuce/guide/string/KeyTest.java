package io.github.weipeng2k.lettuce.guide.string;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

/**
 * @author weipeng2k 2022-11-12 21:41:25
 */
public class KeyTest {

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
        String set = redisCommands.set("x", "y");
        Assert.assertEquals("OK", set);
    }

    @Test
    public void keys() {
        List<String> keys = redisCommands.keys("*");
        keys.forEach(System.out::println);
        Assert.assertTrue(keys.size() > 0);
    }

    @Test
    public void exists() {
        Long x = redisCommands.exists("x");
        Assert.assertEquals(1, x.intValue());
    }

    @Test
    public void delete_key() {
        Long x = redisCommands.del("x");
        Assert.assertEquals(1, x.intValue());
    }

    @Test
    public void type() {
        String x = redisCommands.type("x");
        Assert.assertEquals("string", x);
    }

}

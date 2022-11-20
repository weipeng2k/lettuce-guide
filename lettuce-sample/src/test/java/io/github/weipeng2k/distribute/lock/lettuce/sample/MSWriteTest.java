package io.github.weipeng2k.distribute.lock.lettuce.sample;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.RedisChannelHandler;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisConnectionStateListener;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.masterreplica.MasterReplica;
import io.lettuce.core.masterreplica.StatefulRedisMasterReplicaConnection;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * @author weipeng2k 2022-11-05 23:25:48
 */
public class MSWriteTest {
    @Test
    @Ignore
    public void write() {
        RedisClient redisClient1 = RedisClient.create("redis://weipeng2k-workstation:6380");
        // 创建链接，该链接线程安全
        StatefulRedisConnection<String, String> connection1 = redisClient1.connect();

        // 同步执行RedisCommand
        RedisCommands<String, String> syncCommands1 = connection1.sync();

        RedisClient redisClient2 = RedisClient.create("redis://weipeng2k-workstation:6381");
        // 创建链接，该链接线程安全
        StatefulRedisConnection<String, String> connection2 = redisClient2.connect();
        // 同步执行RedisCommand
        RedisCommands<String, String> syncCommands2 = connection2.sync();

        RedisClient redisClient3 = RedisClient.create("redis://weipeng2k-workstation:6382");
        // 创建链接，该链接线程安全
        StatefulRedisConnection<String, String> connection3 = redisClient3.connect();
        // 同步执行RedisCommand
        RedisCommands<String, String> syncCommands3 = connection3.sync();

        AtomicReference<RedisCommands<String, String>> ref = new AtomicReference<>();
        ref.set(syncCommands1);

        connection1.addListener(new RedisConnectionStateListener() {
            @Override
            public void onRedisDisconnected(RedisChannelHandler<?, ?> connection) {
                RedisConnectionStateListener.super.onRedisDisconnected(connection);
                System.out.println("------------------------------------change");
                syncCommands2.replicaofNoOne();
                syncCommands3.replicaof("weipeng2k-workstation", 6381);
                ref.set(syncCommands2);
                System.out.println("------------------------------------change 2");
            }
        });


        IntStream.range(0, Integer.MAX_VALUE)
                .forEach(i -> {
//                    try {
////                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                    try {
                        ref.get().set("key" + i, "value" + i);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
    }
}

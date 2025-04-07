package com.ll.nbe344team7.global.config.reddison.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.ConcurrentModificationException;
import java.util.concurrent.TimeUnit;

/**
 * redis 분산
 *
 * @author jyson
 * @since 25. 4. 7.
 */
@Service
public class DistributedLock {
    private final RedissonClient redissonClient;

    public DistributedLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void executeWithLock(Long roomId, Runnable action) {
        RLock lock = redissonClient.getLock("chat_lock:" + roomId);
        try {
            if(lock.tryLock(10, 60, TimeUnit.SECONDS))
                action.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ConcurrentModificationException("락 획득 실패");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

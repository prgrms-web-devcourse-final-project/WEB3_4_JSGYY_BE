package com.ll.nbe344team7.global.config.reddison.lock;

import org.mockito.internal.util.Supplier;
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

    public <T> void executeWithLock(Long roomId, Supplier<T> action) {
        RLock lock = redissonClient.getLock("chat_lock:" + roomId);
        try {
            boolean isLocked = lock.tryLock(10, 60, TimeUnit.SECONDS);
            if (!isLocked) throw new ConcurrentModificationException("락 획득 실패");
            action.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Lock acquisition interrupted", e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}

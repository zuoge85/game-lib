package org.forkjoin.core.dao;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 进程内缓存
 * 关于一致性需要注意
 * 线程安全，但是一致性需要注意，肯定会有脏数据！
 *
 * @author zuoge85 on 15/5/9.
 */

@Service
public class LocalCache<T extends EntityObject, K extends KeyObject> {
    private static final Logger log = LoggerFactory.getLogger(LocalCache.class);

    public static final int MAX_MINUTES = 5;
    public static final int MAX_NUMS = 500;

    private Cache<K, T> localCache;

    @PostConstruct
    public void init() {
        /**
         * 弱引用效果需要注意，看源代码有疑问
         */
        localCache = CacheBuilder.newBuilder().expireAfterAccess(
                MAX_MINUTES, TimeUnit.MINUTES
        ).recordStats().maximumSize(MAX_NUMS).weakKeys().build();
    }

    public T get(K k) {
        return localCache.getIfPresent(k);
    }

    public void remove(K k) {
        localCache.invalidate(k);
    }

    public void put(K k, T t) {
        localCache.put(k, t);
    }

    @SuppressWarnings("unchecked")
    public void put(Iterable<T> values) {
        for (T t : values) {
            localCache.put((K) t.getKey(), t);
        }
    }

    public ArrayList<T> getValues(Iterable<K> keys) {
        ArrayList<T> list = new ArrayList<>();
        for (K k : keys) {
            T t = get(k);
            if (t != null) {
                list.add(t);
            }
        }
        return list;
    }

    public CacheStats stats() {
        return localCache.stats();
    }
}

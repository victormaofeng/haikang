package top.maof.haikang.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * 自定义缓存管理器
 */
public class RedisCacheManager implements CacheManager {

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return new RedisCache<K, V>(s);
    }
}

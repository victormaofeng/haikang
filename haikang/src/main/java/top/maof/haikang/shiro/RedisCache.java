package top.maof.haikang.shiro;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.maof.haikang.utils.JWTUtil;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * shiro
 * 自定义Redis缓存
 *
 * @param <K>
 * @param <V>
 */

@Slf4j
@Data
public class RedisCache<K, V> implements Cache<K, V> {

    // shiro缓存名字
    public static String ShiroCacheName;

    @Autowired
    private RedisTemplate redisTemplate;


    public RedisCache(String shiroCacheName) {
        ShiroCacheName = shiroCacheName;
        // log.info("=========================ShiroCacheName:" + ShiroCacheName);
    }

    @Override
    public V get(K k) throws CacheException {
        log.info("get:" + k.toString());
        return (V) redisTemplate.opsForValue().get(ShiroCacheName + ":" + k.toString());
    }

    @Override
    public V put(K k, V v) throws CacheException {
        log.info("put:" + k.toString() + "-" + v.toString());
        redisTemplate.opsForValue().set(ShiroCacheName + ":" + k.toString(), v, JWTUtil.HOUR, TimeUnit.HOURS);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        redisTemplate.delete(ShiroCacheName + ":" + k.toString());
        return null;
    }

    @Override
    public void clear() throws CacheException {
        // redisTemplate.delete(ShiroCacheName + ":" + k.toString());
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}

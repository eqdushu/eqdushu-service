package com.eqdushu.server.cache;


import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 缓存key序列化  配置文件spring-cache.xml中配置redisTemplate
 * @author ray4w
 *
 */
@SuppressWarnings("rawtypes")
public class KeyRedisSerializer implements RedisSerializer {

    private final StringRedisSerializer _serializer = new StringRedisSerializer();


    @Override
    public byte[] serialize(Object o) throws SerializationException {
        byte[] bytes;
        if (o != null) {
        	String key;
        	if(o instanceof SimpleKey){
        		key = String.valueOf(((SimpleKey)o).hashCode());
        	}else {
        		key = o.toString();
        	}
            bytes = _serializer.serialize(key);
        } else {
            bytes = null;
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        Object result;
        if (bytes != null && bytes.length > 0) {
            result = _serializer.deserialize(bytes);
        } else {
            result = null;
        }
        return result;
    }
}

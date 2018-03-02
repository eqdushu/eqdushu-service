package com.eqdushu.server.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 缓存value序列化 配置文件spring-cache.xml中配置redisTemplate
 * @author ray4w
 *
 */

public class FastJsonRedisSerializer implements RedisSerializer<Object> {

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
    	if(bytes == null){
    		return null;
    	}
        return JSON.parse(bytes, Feature.IgnoreNotMatch);
    }

    @Override
    public byte[] serialize(Object t) throws SerializationException {
    	if(t== null){
    		return null;
    	}
        return JSON.toJSONBytes(t, SerializerFeature.WriteClassName);
    }

}
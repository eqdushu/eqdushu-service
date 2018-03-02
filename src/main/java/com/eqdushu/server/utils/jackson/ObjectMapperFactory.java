package com.eqdushu.server.utils.jackson;


import java.util.Date;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperFactory {


    private static final ObjectMapper _jsonMapper = new ObjectMapper();

    static {
        addModules(_jsonMapper);
        _jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper get() {
        return _jsonMapper;
    }

    public static void addModules(ObjectMapper objectMapper) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, DateSerializer.getInstance());
        module.addDeserializer(Date.class, DateDeserializer.getInstance());
        module.addSerializer(Stream.class, Java8StreamSerializer.getInstance());
        objectMapper.registerModule(module);

    }
}

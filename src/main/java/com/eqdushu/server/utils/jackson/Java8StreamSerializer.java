package com.eqdushu.server.utils.jackson;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

public class Java8StreamSerializer extends StdSerializer<Stream> {

    private final static Java8StreamSerializer _instance = new Java8StreamSerializer();

    private Java8StreamSerializer() {
        super(Stream.class, true);
    }

    @Override
    public void serialize(Stream stream, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        provider.findValueSerializer(Iterator.class, null).serialize(stream.iterator(), jgen, provider);
    }

    public static Java8StreamSerializer getInstance() {
        return _instance;
    }
}

package com.eqdushu.server.utils.jackson;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.eqdushu.server.utils.date.DateTimeUtil;

public class DateSerializer extends JsonSerializer<Date> {

    private final static DateSerializer _instance = new DateSerializer();

    private DateSerializer(){

    }

    @Override
    public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        String dateString;
        if (!DateTimeUtil.timeExists(value)) {
            dateString = DateTimeUtil.formatAsYYYYMMdd(value);
        } else {
            dateString = DateTimeUtil.formatAsYYYYMMddHHmmss(value);
        }

        jgen.writeString(dateString);
    }

    public static DateSerializer getInstance() {
        return _instance;
    }
}
package com.eqdushu.server.utils.jackson;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.eqdushu.server.utils.date.DateTimeUtil;

public class DateDeserializer extends JsonDeserializer<Date> {

    private final static DateDeserializer _instance = new DateDeserializer();

    private DateDeserializer(){}

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String dateString = jp.getText();
        Date date;
        if(dateString.length() == 10){
            date = DateTimeUtil.parseAsYYYYMMdd(dateString);
        }else if(dateString.length() == 19){
            date = DateTimeUtil.parseAsYYYYMMddHHmmss(dateString);
        }else{
            date = null;
        }
        if(date == null)
            throw new IllegalStateException("illegal date format:"+ dateString);
        else
            return date;
    }

    public static DateDeserializer getInstance(){
        return _instance;
    }
}
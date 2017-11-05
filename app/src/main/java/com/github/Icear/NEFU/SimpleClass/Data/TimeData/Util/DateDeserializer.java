package com.github.Icear.NEFU.SimpleClass.Data.TimeData.Util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Date;

/**
 * Created by icear on 2017/11/4.
 * java.sql.Date类的Json反序列化函数
 */

public class DateDeserializer implements JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Date date;
        date = Date.valueOf(json.getAsString());//format yyyy-[m]m-[d]d
        return date;
    }
}

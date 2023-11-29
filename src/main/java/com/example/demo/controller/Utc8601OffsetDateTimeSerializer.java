package com.example.demo.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Utc8601OffsetDateTimeSerializer  extends JsonSerializer<OffsetDateTime> {

    private static final DateTimeFormatter utc8601Format = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_ZONED_DATE_TIME)
            .toFormatter();

    @Override
    public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String dateFormatAsString = utc8601Format.format(offsetDateTime);
        jsonGenerator.writeString(dateFormatAsString);

    }
}

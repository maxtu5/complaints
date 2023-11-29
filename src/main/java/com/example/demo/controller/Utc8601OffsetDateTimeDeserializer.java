package com.example.demo.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Utc8601OffsetDateTimeDeserializer extends InstantDeserializer<Instant> {
    public Utc8601OffsetDateTimeDeserializer() {
        super(InstantDeserializer.INSTANT,
                new DateTimeFormatterBuilder().
                        parseCaseInsensitive()
                        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .optionalStart().appendOffset("+HH:MM",
                                "+00:00").optionalEnd().toFormatter());
    }
}

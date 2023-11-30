package com.complaints.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utc8601InstantSerializer extends JsonSerializer<Instant> {

    private static final DateTimeFormatter iso8601Formatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSxxx")
            .withZone(ZoneId.of("UTC"));

    @Override
    public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            throw new IOException("Instant argument is null.");
        }
        gen.writeString(iso8601Formatter.format(value));
    }
}
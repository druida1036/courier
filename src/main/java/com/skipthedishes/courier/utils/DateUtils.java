package com.skipthedishes.courier.utils;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

    public static OffsetDateTime toDateTime(final Long date) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneOffset.UTC);
    }
}

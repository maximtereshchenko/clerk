package com.github.maximtereshchenko.test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public final class ManualClock extends Clock {

    private Instant instant = Instant.parse("2020-01-01T00:00:00Z");

    @Override
    public ZoneId getZone() {
        return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return this;
    }

    @Override
    public Instant instant() {
        return instant;
    }

    public void waitOneHour() {
        instant = instant.plus(1, ChronoUnit.HOURS);
    }
}

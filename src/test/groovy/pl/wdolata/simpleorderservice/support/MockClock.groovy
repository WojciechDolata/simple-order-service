package pl.wdolata.simpleorderservice.support

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class MockClock extends Clock {
    private Instant instant
    private final ZoneId zone

    MockClock(Instant instant, ZoneId zone = ZoneId.of('UTC')) {
        this.instant = instant
        this.zone = zone
    }

    void setInstant(Instant instant) {
        this.instant = instant
    }

    @Override
    ZoneId getZone() {
        return zone
    }

    @Override
    Clock withZone(ZoneId zone) {
        return new MockClock(instant, zone)
    }

    @Override
    Instant instant() {
        return instant
    }

}

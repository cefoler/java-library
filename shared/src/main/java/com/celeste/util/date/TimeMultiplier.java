package com.celeste.util.date;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeMultiplier {

    SECONDS(1000, 's'),
    MINUTES(60 * 1000, 'm'),
    HOURS(60 * 60 * 1000, 'h'),
    DAYS(24 * 60 * 60 * 1000, 'd'),
    WEEKS(7 * 24 * 60 * 60 * 1000, 'w');
    // MONTHS(30L * 24 * 60 * 60 * 1000, 'm'),
    // YEARS(365L * 24 * 60 * 60 * 1000, 'y');

    private final long multiplier;
    private final char diminutive;

}

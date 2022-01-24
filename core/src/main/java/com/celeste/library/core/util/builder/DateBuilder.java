package com.celeste.library.core.util.builder;

import com.celeste.library.core.util.formatter.DateFormatter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateBuilder {

  private DateTimeFormatter dateFormat;

  public static DateBuilder builder() {
    return new DateBuilder();
  }

  public DateBuilder format(final DateTimeFormatter format) {
    this.dateFormat = format;
    return setTimezone();
  }

  public DateBuilder format(final String format) {
    this.dateFormat = DateTimeFormatter.ofPattern(format);
    return setTimezone();
  }

  private DateBuilder setTimezone() {
    setTimezone(ZoneId.systemDefault());
    return this;
  }

  private DateBuilder setTimezone(final ZoneId zoneId) {
    dateFormat.withZone(zoneId);
    return this;
  }

  public String convert(final long time) {
    return DateFormatter.convertToString(time, dateFormat);
  }

}

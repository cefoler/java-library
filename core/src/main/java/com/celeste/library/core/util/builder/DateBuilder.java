package com.celeste.library.core.util.builder;

import com.celeste.library.core.util.formatter.DateFormatter;
import com.celeste.library.core.util.formatter.type.TimeLanguage;
import com.celeste.library.core.util.pattern.RegexPattern;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateBuilder {

  private SimpleDateFormat dateFormat;

  public static DateBuilder builder() {
    return new DateBuilder();
  }

  public DateBuilder format(final SimpleDateFormat format) {
    this.dateFormat = format;
    return setTimezone();
  }

  public DateBuilder format(final String format) {
    this.dateFormat = new SimpleDateFormat(format);
    return setTimezone();
  }

  private DateBuilder setTimezone() {
    dateFormat.setTimeZone(TimeZone.getDefault());
    return this;
  }

  private DateBuilder setTimezone(final TimeZone timezone) {
    dateFormat.setTimeZone(timezone);
    return this;
  }

  public String convert(final long time) {
    return DateFormatter.convertToString(time, dateFormat);
  }

  public String convert(final Date date) {
    return DateFormatter.convertToString(date, dateFormat);
  }

}

package com.celeste.library.core.util.formatter.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeLanguage {

  ENGLISH("days", "day", "hours", "hour", "minutes", "minute", "seconds", "second", "and"),
  PORTUGUESE("dias", "dia", "horas", "hora", "minutos", "minuto", "segundos", "segundo", "e");

  private final String days;
  private final String day;

  private final String hours;
  private final String hour;

  private final String minutes;
  private final String minute;

  private final String seconds;
  private final String second;

  private final String and;

}

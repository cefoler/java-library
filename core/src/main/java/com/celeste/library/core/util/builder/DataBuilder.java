package com.celeste.library.core.util.builder;

import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBuilder extends Properties {

  private DataBuilder(final Properties properties) {
    super(properties);
  }

  public static synchronized DataBuilder create() {
    return new DataBuilder();
  }

  public static synchronized DataBuilder create(final Properties properties) {
    return new DataBuilder(properties);
  }

  public synchronized DataBuilder set(final Object key, final Object value) {
    put(String.valueOf(key), value);
    return this;
  }

  public synchronized DataBuilder setIfAbsent(final Object key, final Object value) {
    putIfAbsent(String.valueOf(key), value);
    return this;
  }

  public synchronized boolean contains(final Object key) {
    return containsKey(key);
  }

  @SuppressWarnings("unchecked")
  public synchronized <T> T getData(final String key) {
    return (T) get(key);
  }

  @SuppressWarnings("unchecked")
  public synchronized <T> T getData(final String key, final Object orElse) {
    final T value = getData(key);
    return value != null ? value : (T) orElse;
  }

}

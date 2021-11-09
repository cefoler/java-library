package com.celeste.library.core.util.builder;

import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Synchronized;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DataBuilder extends Properties {

  private DataBuilder(final Properties properties) {
    super(properties);
  }

  @Synchronized
  public static DataBuilder create() {
    return new DataBuilder();
  }

  @Synchronized
  public static DataBuilder create(final Properties properties) {
    return new DataBuilder(properties);
  }

  @Synchronized
  public DataBuilder set(final Object key, final Object value) {
    put(String.valueOf(key), value);
    return this;
  }

  @Synchronized
  public DataBuilder setIfAbsent(final Object key, final Object value) {
    putIfAbsent(String.valueOf(key), value);
    return this;
  }

  @Synchronized
  public boolean contains(final Object key) {
    return containsKey(key);
  }

  @Synchronized @SuppressWarnings("unchecked")
  public <T> T getData(final String key) {
    return (T) get(key);
  }

  @Synchronized @SuppressWarnings("unchecked")
  public <T> T getData(final String key, final Object orElse) {
    final T value = getData(key);
    return value != null ? value : (T) orElse;
  }

}

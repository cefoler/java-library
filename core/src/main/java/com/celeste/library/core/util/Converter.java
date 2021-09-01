package com.celeste.library.core.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import com.celeste.library.core.util.pattern.CollectorPattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Converter {

  public static String[] toLowerCase(final String... strings) {
    return Arrays.stream(strings)
        .map(String::toLowerCase)
        .toArray(String[]::new);
  }

  public static List<String> toLowerCase(final List<String> list) {
    return list.stream()
        .map(String::toLowerCase)
        .collect(CollectorPattern.toList());
  }

  public static List<String> toLowerCase(final Set<String> set) {
    return set.stream()
        .map(String::toLowerCase)
        .collect(CollectorPattern.toList());
  }

  public static <T> Map<T, String> toLowerCase(final Map<T, String> map) {
    return convert(map, entry -> {
      final String value = entry.getValue();
      entry.setValue(value.toLowerCase());
    });
  }

  public static String[] toUpperCase(final String... strings) {
    return Arrays.stream(strings)
        .map(String::toUpperCase)
        .toArray(String[]::new);
  }

  public static List<String> toUpperCase(final List<String> list) {
    return list.stream()
        .map(String::toUpperCase)
        .collect(CollectorPattern.toList());
  }

  public static List<String> toUpperCase(final Set<String> set) {
    return set.stream()
        .map(String::toUpperCase)
        .collect(CollectorPattern.toList());
  }

  public static <T> Map<T, String> toUpperCase(final Map<T, String> map) {
    return convert(map, entry -> {
      final String value = entry.getValue();
      entry.setValue(value.toUpperCase());
    });
  }

  private static <T> Map<T, String> convert(final Map<T, String> map,
                                            final Consumer<Entry<T, String>> consumer) {
    return map.entrySet().stream()
        .peek(consumer)
        .collect(CollectorPattern.toMap(Entry::getKey, Entry::getValue, () -> {
          try {
            return Reflection.instance(map);
          } catch (ReflectiveOperationException exception) {
            return new ConcurrentHashMap<>();
          }
        }));
  }

}

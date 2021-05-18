package com.celeste.library.core.model.registry;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public abstract class AbstractRegistry<T, U> implements Registry<T, U> {

  private final Map<T, U> map;

  @Override
  public U register(final T key, final U value) {
    return map.put(key, value);
  }

  @Override
  public U registerIfAbsent(final T key, final U value) {
    return map.putIfAbsent(key, value);
  }

  @Override
  public void registerAll(final Map<T, U> values) {
    map.putAll(values);
  }

  @Override
  public U remove(final T key) {
    return map.remove(key);
  }

  @Override
  public U replace(final T key, final U value) {
    return map.replace(key, value);
  }

  @Override
  @Nullable
  public U get(final T key) {
    return map.get(key);
  }

  @Override
  public boolean contains(final T key) {
    return map.containsKey(key);
  }

  @Override
  public Set<Entry<T, U>> getEntrySet() {
    return map.entrySet();
  }

  @Override
  public Set<T> getKeys() {
    return map.keySet();
  }

  @Override
  public Collection<U> getAll() {
    return map.values();
  }

  @Override
  public List<U> sort(final Comparator<U> comparator) {
    return getAll()
        .stream()
        .sorted(comparator)
        .collect(Collectors.toList());
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public void wipe() {
    map.clear();
  }

  @Override
  public Map<T, U> getMap() {
    return map;
  }

  @Override
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public Registry<T, U> clone() {
    return (Registry<T, U>) super.clone();
  }

}

package com.celeste.library.spigot.util.menu;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum BorderShape {

  THREE(
      10, 11, 12, 13, 14, 15, 16),
  FOUR(
      10, 11, 12, 13, 14, 15, 16,
      19, 20, 21, 22, 23, 24, 25),
  FIVE(
      10, 11, 12, 13, 14, 15, 16,
      19, 20, 21, 22, 23, 24, 25,
      28, 29, 30, 31, 32, 33, 34),
  SIX(
      10, 11, 12, 13, 14, 15, 16,
      19, 20, 21, 22, 23, 24, 25,
      28, 29, 30, 31, 32, 33, 34,
      37, 38, 39, 40, 41, 42, 43);

  private final Integer[] shape;

  BorderShape(final Integer... shape) {
    this.shape = ImmutableList.copyOf(shape).toArray(new Integer[0]);
  }

}

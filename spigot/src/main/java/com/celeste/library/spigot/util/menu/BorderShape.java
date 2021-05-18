package com.celeste.library.spigot.util.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BorderShape {

  // Inventory with 27 slots
  THREE(new Integer[]{
      10, 11, 12, 13, 14, 15, 16
  }),
  // Inventory with 36 slots
  FOUR(new Integer[]{
      10, 11, 12, 13, 14, 15, 16,
      19, 20, 21, 22, 23, 24, 25
  }),
  // Inventory with 46 slots
  FIVE(new Integer[]{
      10, 11, 12, 13, 14, 15, 16,
      19, 20, 21, 22, 23, 24, 25,
      28, 29, 30, 31, 32, 33, 34
  }),
  // Inventory with 54 slots
  SIX(new Integer[]{
      10, 11, 12, 13, 14, 15, 16,
      19, 20, 21, 22, 23, 24, 25,
      28, 29, 30, 31, 32, 33, 34,
      37, 38, 39, 40, 41, 42, 43
  });

  private final Integer[] shape;

}

package com.celeste.library.spigot.util.item.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WoolType {

  WHITE(0),
  ORANGE(1),
  MAGENTA(2),
  LIGHT_BLUE(3),
  YELLOW(4),
  LIME(5),
  PINK(6),
  GRAY(7),
  LIGHT_GRAY(8),
  CYAN(9),
  PURPLE(10),
  BLUE(11),
  BROWN(12),
  GREEN(13),
  RED(14),
  BLACK(15);

  private final int data;

  public static WoolType get(final int data) {
    return Arrays.stream(values())
        .filter(woolType -> woolType.getData() == data)
        .findFirst()
        .orElse(null);
  }

  public static WoolType getOrThrow(final int data) {
    return Arrays.stream(values())
        .filter(woolType -> woolType.getData() == data)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("The used wool type was not found."));
  }

}

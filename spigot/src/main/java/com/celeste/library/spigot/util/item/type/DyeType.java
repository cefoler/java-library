package com.celeste.library.spigot.util.item.type;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public enum DyeType {

  BLACK(0),
  RED(1),
  GREEN(2),
  BROWN(3),
  DARK_BLUE(4),
  PURPLE(5),
  CYAN(6),
  LIGHT_GRAY(7),
  GRAY(8),
  PINK(9),
  LIME(10),
  YELLOW(11),
  LIGHT_BLUE(12),
  MAGENTA(13),
  ORANGE(14),
  WHITE(15);

  private final int data;

  @Nullable
  public static DyeType get(final int data) {
    return Arrays.stream(values())
        .filter(dyeType -> dyeType.getData() == data)
        .findFirst()
        .orElse(null);
  }

  public static DyeType getOrThrow(final int data) {
    return Arrays.stream(values())
        .filter(dyeType -> dyeType.getData() == data)
        .findFirst()
        .orElseThrow(() -> new UnsupportedOperationException("The used dye type was not found."));
  }

  @Nullable
  public static DyeType getByRecipe(final int principal, final int secondary) {
    final DyeType type = get(principal);
    if (type == null) {
      return null;
    }

    switch (type) {
      case RED:
        return secondary == YELLOW.getData()
            ? ORANGE : secondary == WHITE.getData()
            ? PINK : PURPLE;
      case DARK_BLUE:
        return secondary == RED.getData()
            ? PURPLE : secondary == GREEN.getData()
            ? CYAN : LIGHT_BLUE;
      case WHITE:
        return secondary == GRAY.getData()
            ? LIGHT_GRAY : secondary == RED.getData()
            ? PINK : secondary == DARK_BLUE.getData()
            ? LIGHT_BLUE : secondary == BLACK.getData()
            ? GRAY : LIME;
      case BLACK:
        return GRAY;
      case GREEN:
        return secondary == WHITE.getData() ? LIME : CYAN;
      case YELLOW:
        return ORANGE;
    }

    return null;
  }

  public static DyeType[] getRecipe(final DyeType dyeType) {
    switch (dyeType) {
      case ORANGE:
        return new DyeType[]{RED, YELLOW};
      case PINK:
        return new DyeType[]{RED, WHITE};
      case PURPLE:
        return new DyeType[]{DARK_BLUE, RED};
      case CYAN:
        return new DyeType[]{DARK_BLUE, GREEN};
      case LIGHT_BLUE:
        return new DyeType[]{DARK_BLUE, WHITE};
      case LIGHT_GRAY:
        return new DyeType[]{WHITE, GRAY};
      case GRAY:
        return new DyeType[]{BLACK, WHITE};
      case LIME:
        return new DyeType[]{GREEN, WHITE};
      default:
        return null;
    }
  }

}

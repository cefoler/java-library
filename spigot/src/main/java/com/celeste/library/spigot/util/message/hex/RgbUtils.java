package com.celeste.library.spigot.util.message.hex;

import com.celeste.library.spigot.util.ReflectionNms;
import com.google.common.collect.ImmutableMap;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;

/**
 * Can be used such as <f0fa3d>This is my message
 */
public final class RgbUtils {

  private static final int VERSION;
  private static final boolean SUPPORTS_RGB;
  private static final List<String> SPECIAL_COLORS;
  private static final Map<Color, ChatColor> COLORS;
  private static final Pattern PATTERN;

  static {
    VERSION = ReflectionNms.getVersion();
    SUPPORTS_RGB = VERSION >= 16;

    SPECIAL_COLORS = Arrays.asList("&l", "&n", "&o", "&k", "&m",
        "§l", "§n", "§o", "§k", "§m");

    COLORS = ImmutableMap.<Color, ChatColor>builder()
        .put(new Color(0), ChatColor.getByChar('0'))
        .put(new Color(170), ChatColor.getByChar('1'))
        .put(new Color(43520), ChatColor.getByChar('2'))
        .put(new Color(43690), ChatColor.getByChar('3'))
        .put(new Color(11141120), ChatColor.getByChar('4'))
        .put(new Color(11141290), ChatColor.getByChar('5'))
        .put(new Color(16755200), ChatColor.getByChar('6'))
        .put(new Color(11184810), ChatColor.getByChar('7'))
        .put(new Color(5592405), ChatColor.getByChar('8'))
        .put(new Color(5592575), ChatColor.getByChar('9'))
        .put(new Color(5635925), ChatColor.getByChar('a'))
        .put(new Color(5636095), ChatColor.getByChar('b'))
        .put(new Color(16733525), ChatColor.getByChar('c'))
        .put(new Color(16733695), ChatColor.getByChar('d'))
        .put(new Color(16777045), ChatColor.getByChar('e'))
        .put(new Color(16777215), ChatColor.getByChar('f'))
        .build();

    PATTERN = Pattern.compile("<([0-9A-Fa-f]{6})>|#\\{([0-9A-Fa-f]{6})}");
  }

  public static String process(final String string) {
    return ChatColor.translateAlternateColorCodes('&', processSolid(string));
  }

  private static String processSolid(String string) {
    final Matcher matcher = PATTERN.matcher(string);
    while (matcher.find()) {
      String color = matcher.group(1);
      if (color == null) {
        color = matcher.group(2);
      }

      string = string.replace(matcher.group(), RgbUtils.getColor(color) + "");
    }

    return string;
  }

  public static List<String> process(final List<String> strings) {
    return strings.stream()
        .map(RgbUtils::process)
        .collect(Collectors.toList());
  }

  public static List<String> process(final String... strings) {
    return process(Arrays.asList(strings));
  }

  public static String color(final String string, final Color start, final Color end) {
    final ChatColor[] colors = createGradient(start, end, withoutSpecialChar(string).length());
    return apply(string, colors);
  }

  public static String rainbow(final String string, final float saturation) {
    final ChatColor[] colors = createRainbow(withoutSpecialChar(string).length(), saturation);
    return apply(string, colors);
  }

  public static ChatColor getColor(String string) {
    return SUPPORTS_RGB
        ? ChatColorSupport.of(new Color(Integer.parseInt(string, 16)))
        : getClosestColor(new Color(Integer.parseInt(string, 16)));
  }

  public static String stripColorFormatting(final String string) {
    return string.replaceAll(
        "<#[0-9A-F]{6}>|[&§][a-f0-9lnokm]|<[/]?[A-Z]{5,8}(:[0-9A-F]{6})?[0-9]*>", "");
  }

  private static String apply(final String source, ChatColor[] colors) {
    final StringBuilder specialColors = new StringBuilder();
    final StringBuilder builder = new StringBuilder();

    final String[] characters = source.split("");
    int outIndex = 0;
    for (int index = 0; index < characters.length; index++) {
      if (!characters[index].equals("&") && !characters[index].equals("§")) {
        builder.append(colors[outIndex++]).append(specialColors).append(characters[index]);
        continue;
      }

      if (index + 1 > characters.length) {
        builder.append(colors[outIndex++]).append(specialColors).append(characters[index]);
        continue;
      }

      if (characters[index + 1].equals("r")) {
        specialColors.setLength(0);
      } else {
        specialColors.append(characters[index]);
        specialColors.append(characters[index + 1]);
      }

      index++;
    }

    return builder.toString();
  }

  private static String withoutSpecialChar(final String source) {
    final AtomicReference<String> value = new AtomicReference<>(source);
    SPECIAL_COLORS.forEach(color -> {
      final String valueString = value.get();
      if (valueString.contains(color)) {
        value.set(valueString.replace(color, ""));
      }
    });

    return value.get();
  }

  private static ChatColor[] createRainbow(final int step, final float saturation) {
    final ChatColor[] colors = new ChatColor[step];
    final double colorStep = (1.00 / step);

    for (int i = 0; i < step; i++) {
      final Color color = Color.getHSBColor((float) (colorStep * i), saturation, saturation);
      if (SUPPORTS_RGB) {
        colors[i] = ChatColorSupport.of(color);
        continue;
      }

      colors[i] = getClosestColor(color);
    }

    return colors;
  }

  private static ChatColor[] createGradient(final Color start, final Color end, final int step) {
    final ChatColor[] colors = new ChatColor[step];

    final int stepR = Math.abs(start.getRed() - end.getRed()) / (step - 1);
    final int stepG = Math.abs(start.getGreen() - end.getGreen()) / (step - 1);
    final int stepB = Math.abs(start.getBlue() - end.getBlue()) / (step - 1);

    final int[] direction = new int[]{
        start.getRed() < end.getRed() ? +1 : -1,
        start.getGreen() < end.getGreen() ? +1 : -1,
        start.getBlue() < end.getBlue() ? +1 : -1
    };

    for (int i = 0; i < step; i++) {
      final Color color = new Color(start.getRed() + ((stepR * i) * direction[0]),
          start.getGreen() + ((stepG * i) * direction[1]),
          start.getBlue() + ((stepB * i) * direction[2]));
      if (SUPPORTS_RGB) {
        colors[i] = ChatColorSupport.of(color);
        continue;
      }

      colors[i] = getClosestColor(color);
    }

    return colors;
  }

  private static ChatColor getClosestColor(final Color color) {
    Color nearestColor = null;
    double nearestDistance = Integer.MAX_VALUE;

    for (Color constantColor : COLORS.keySet()) {
      double distance = Math.pow(color.getRed() - constantColor.getRed(), 2) + Math.pow(
          color.getGreen() - constantColor.getGreen(), 2) + Math.pow(
          color.getBlue() - constantColor.getBlue(), 2);

      if (nearestDistance > distance) {
        nearestColor = constantColor;
        nearestDistance = distance;
      }
    }

    return COLORS.get(nearestColor);
  }

}

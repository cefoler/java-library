package com.celeste.library.spigot.util.message.hex;

import com.celeste.library.core.util.Reflection;
import java.awt.Color;
import java.lang.reflect.Constructor;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ChatColor;

public final class ChatColorSupport {

  private static Constructor<?> CHAT_COLOR_CONSTRUCTOR;

  static {
    try {
      final Class<?> chatColorClazz = Reflection.getClazz("net.md_5.bungee.api");

      CHAT_COLOR_CONSTRUCTOR = chatColorClazz.getConstructor(String.class, String.class, int.class);
      CHAT_COLOR_CONSTRUCTOR.setAccessible(true);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @SneakyThrows
  public static ChatColor of(final String string) {
    if (!string.startsWith("#") || string.length() != 7) {
      return ChatColor.valueOf(string.toUpperCase());
    }

    int rgb;
    try {
      rgb = Integer.parseInt(string.substring(1), 16);
    } catch (NumberFormatException exception) {
      throw new IllegalArgumentException("Illegal hex string " + string);
    }

    final StringBuilder builder = new StringBuilder("§x");

    final char[] parsedString = string.substring(1).toCharArray();
    for (char c : parsedString) {
      builder.append('§').append(c);
    }

    return (ChatColor) CHAT_COLOR_CONSTRUCTOR.newInstance(string, builder.toString(), rgb);
  }

  public static ChatColor of(final Color color) {
    return of("#" + String.format("%08x", color.getRGB()).substring(2));
  }

}

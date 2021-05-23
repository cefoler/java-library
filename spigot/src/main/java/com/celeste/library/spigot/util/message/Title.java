package com.celeste.library.spigot.util.message;

import static com.celeste.library.spigot.util.ReflectionNms.sendPacket;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.error.ServerStartError;
import com.celeste.library.spigot.util.ReflectionNms;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Title {

  private static final Constructor<?> PACKET_TIME_CONSTRUCTOR;
  private static final Constructor<?> PACKET_TEXT_CONSTRUCTOR;

  private static final Method A;

  private static final Object TIME;
  private static final Object TITLE;
  private static final Object SUBTITLE;

  static {
    try {
      final Class<?> packetTitleClazz = ReflectionNms.getNms("PacketPlayOutTitle");
      final Class<?> componentClazz = ReflectionNms.getNms("IChatBaseComponent");

      final Class<?> titleActionClazz = Reflection.getDcClasses(packetTitleClazz).length > 0
          ? Reflection.getDcClasses(packetTitleClazz, 0)
          : ReflectionNms.getNms("EnumTitleAction");

      final Class<?> serializer = Reflection.getDcClasses(componentClazz).length > 0
          ? Reflection.getDcClasses(componentClazz, 0)
          : ReflectionNms.getNms("ChatSerializer");

      final Field times = Reflection.getField(titleActionClazz, "TIMES");
      final Field title = Reflection.getField(titleActionClazz, "TITLE");
      final Field subtitle = Reflection.getField(titleActionClazz, "SUBTITLE");

      A = Reflection.getMethod(serializer, "a", String.class);

      PACKET_TIME_CONSTRUCTOR = Reflection.getConstructor(packetTitleClazz, titleActionClazz,
          componentClazz, int.class, int.class, int.class);
      PACKET_TEXT_CONSTRUCTOR = Reflection.getConstructor(packetTitleClazz, titleActionClazz,
          componentClazz);

      TIME = Reflection.getStatic(times);
      TITLE = Reflection.getStatic(title);
      SUBTITLE = Reflection.getStatic(subtitle);
    } catch (Exception exception) {
      throw new ServerStartError(exception.getMessage(), exception.getCause());
    }
  }

  @SneakyThrows
  public static void send(final Player player, final String title, final String subtitle,
      final int timeIn, final int timeShow, final int timeOut) {
    final Object timePacket = Reflection.instance(PACKET_TIME_CONSTRUCTOR, TIME, null, timeIn,
        timeShow, timeOut);

    final Object titleText = Reflection.invokeStatic(A, "{\"text\":\"" + title + "\"}");
    final Object titlePacket = Reflection.instance(PACKET_TEXT_CONSTRUCTOR, TITLE,
        titleText);

    final Object subtitleText = Reflection.invokeStatic(A, "{\"text\":\"" + subtitle + "\"}");
    final Object subtitlePacket = Reflection.instance(PACKET_TEXT_CONSTRUCTOR, SUBTITLE,
        subtitleText);

    sendPacket(player, timePacket);
    sendPacket(player, titlePacket);
    sendPacket(player, subtitlePacket);
  }

  @SneakyThrows
  public static void sendAll(final String title, final String subtitle,
      final int timeIn, final int timeShow, final int timeOut) {
    final Object timePacket = Reflection.instance(PACKET_TIME_CONSTRUCTOR, TIME, null, timeIn,
        timeShow, timeOut);

    final Object titleText = Reflection.invokeStatic(A, "{\"text\":\"" + title + "\"}");
    final Object titlePacket = Reflection.instance(PACKET_TEXT_CONSTRUCTOR, TITLE,
        titleText);

    final Object subtitleText = Reflection.invokeStatic(A, "{\"text\":\"" + subtitle + "\"}");
    final Object subtitlePacket = Reflection.instance(PACKET_TEXT_CONSTRUCTOR, SUBTITLE,
        subtitleText);

    for (final Player player : Bukkit.getOnlinePlayers()) {
      sendPacket(player, timePacket);
      sendPacket(player, titlePacket);
      sendPacket(player, subtitlePacket);
    }
  }

}

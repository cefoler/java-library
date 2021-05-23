package com.celeste.library.spigot.util.message;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.error.ServerStartError;
import com.celeste.library.spigot.util.ReflectionNms;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Utility to send ActionBar to a single or multiple players.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionBar {

  private static final Constructor<?> PACKET_CHAT_CONSTRUCTOR;

  private static final Method A;

  private static final Object MESSAGE_TYPE;

  static {
    try {
      final Class<?> packetChatClazz = ReflectionNms.getNms("PacketPlayOutChat");
      final Class<?> componentClazz = ReflectionNms.getNms("IChatBaseComponent");
      final Class<?> messageTypeClazz = ReflectionNms.isEqualsOrMoreRecent(12)
          ? ReflectionNms.getNms("ChatMessageType")
          : byte.class;

      final Class<?> serializer = Reflection.getDcClasses(componentClazz).length > 0
          ? Reflection.getDcClasses(componentClazz, 0)
          : ReflectionNms.getNms("ChatSerializer");

      MESSAGE_TYPE = ReflectionNms.isEqualsOrMoreRecent(12)
          ? messageTypeClazz.getEnumConstants()[2]
          : (byte) 2;

      A = Reflection.getMethod(serializer, "a", String.class);

      PACKET_CHAT_CONSTRUCTOR = ReflectionNms.isEqualsOrMoreRecent(16)
          ? Reflection.getConstructor(packetChatClazz, messageTypeClazz, UUID.class)
          : Reflection.getConstructor(packetChatClazz, messageTypeClazz);
    } catch (Exception exception) {
      throw new ServerStartError(exception.getMessage(), exception.getCause());
    }
  }

  @SneakyThrows
  public static void send(final Player player, final String message) {
    final Object chat = Reflection.invokeStatic(A, "{\"text\":\"" + message + "\"}");

    if (ReflectionNms.isEqualsOrMoreRecent(16)) {
      final Object packet = Reflection.instance(PACKET_CHAT_CONSTRUCTOR, chat, MESSAGE_TYPE,
          player.getUniqueId());
      ReflectionNms.sendPacket(player, packet);
      return;
    }

    final Object packet = Reflection.instance(PACKET_CHAT_CONSTRUCTOR, chat, MESSAGE_TYPE);
    ReflectionNms.sendPacket(player, packet);
  }

  @SneakyThrows
  public static void sendAll(final String message) {
    final Object chat = Reflection.invokeStatic(A, "{\"text\":\"" + message + "\"}");

    if (ReflectionNms.isEqualsOrMoreRecent(16)) {
      for (final Player player : Bukkit.getOnlinePlayers()) {
        final Object packet = Reflection.instance(PACKET_CHAT_CONSTRUCTOR, chat, MESSAGE_TYPE,
            player.getUniqueId());
        ReflectionNms.sendPacket(player, packet);
      }

      return;
    }

    final Object packet = Reflection.instance(PACKET_CHAT_CONSTRUCTOR, chat, MESSAGE_TYPE);
    for (final Player player : Bukkit.getOnlinePlayers()) {
      ReflectionNms.sendPacket(player, packet);
    }
  }

}

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
import org.bukkit.command.CommandSender;
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

      MESSAGE_TYPE = ReflectionNms.isEqualsOrMoreRecent(12)
          ? messageTypeClazz.getEnumConstants()[2]
          : (byte) 2;

      final Class<?>[] componentClasses = Reflection.getDcClasses(componentClazz);
      A = componentClasses.length > 0
          ? Reflection.getMethod(componentClasses[0], "a", String.class)
          : Reflection.getMethod(ReflectionNms.getNms("ChatSerializer"), "a", String.class);

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
  public final void sendAll(final String message) {
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

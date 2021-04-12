package com.celeste.library.spigot.util.message;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;

import static com.celeste.library.spigot.util.ReflectionUtil.*;

/**
 * Utility to send ActionBar to
 * a single or multiple players.
 */
@Getter
public final class ActionBarUtil {

  private static final ActionBarUtil instance = new ActionBarUtil();

  private final Constructor<?> ppocCon;
  private final Method method;

  private final Object type;

  @SneakyThrows
  public ActionBarUtil() {
    final Class<?> cmtClass;
    final Class<?> icbcClass = getNMS("IChatBaseComponent");
    final Class<?> ppocClass = getNMS("PacketPlayOutChat");

    if (isEqualsOrMoreRecent(12)) {
      cmtClass = getNMS("ChatMessageType");
      type = cmtClass.getEnumConstants() [2];
    } else {
      cmtClass = byte.class;
      type = (byte) 2;
    }

    if (icbcClass.getDeclaredClasses().length > 0) {
      method = icbcClass.getDeclaredClasses() [0].getMethod("a", String.class);
    } else {
      method = getNMS("ChatSerializer").getMethod("a", String.class);
    }

    if (isEqualsOrMoreRecent(16)) {
      ppocCon = ppocClass.getConstructor(icbcClass, cmtClass, UUID.class);
    } else ppocCon = ppocClass.getConstructor(icbcClass, cmtClass);
  }

  @SneakyThrows
  public final void send(final CommandSender sender, @NotNull final String message) {
    if (!(sender instanceof Player)) return;

    final Player player = (Player) sender;
    if (isEqualsOrMoreRecent(16)) {
      final Object chatBase = method.invoke(null, "{\"text\":\"" + message + "\"}");
      final Object packet = ppocCon.newInstance(chatBase, type, player.getUniqueId());
      sendPacket(player, packet);
      return;
    }

    final Object chatBase = method.invoke(null, "{\"text\":\"" + message + "\"}");
    final Object packet = ppocCon.newInstance(chatBase, type);
    sendPacket(player, packet);
  }

  @SneakyThrows
  public final void sendAll(final String path, @NotNull final String message) {
    if (isEqualsOrMoreRecent(16)) {
      final Object chatBase = method.invoke(null, "{\"text\":\"" + message + "\"}");

      for (final Player player : Bukkit.getOnlinePlayers()) {
        final Object packet = ppocCon.newInstance(chatBase, type, player.getUniqueId());
        sendPacket(player, packet);
      }

      return;
    }

    final Object chatBase = method.invoke(null, "{\"text\":\"" + message + "\"}");
    final Object packet = ppocCon.newInstance(chatBase, type);

    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
  }

}

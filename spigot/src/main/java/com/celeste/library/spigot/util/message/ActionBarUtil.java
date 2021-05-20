package com.celeste.library.spigot.util.message;

import static com.celeste.library.spigot.util.ReflectionNms.getNms;
import static com.celeste.library.spigot.util.ReflectionNms.isEqualsOrMoreRecent;
import static com.celeste.library.spigot.util.ReflectionNms.sendPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.UUID;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Utility to send ActionBar to a single or multiple players.
 */
@Getter
public final class ActionBarUtil {

  public static final ActionBarUtil INSTANCE = new ActionBarUtil();

  private final Constructor<?> constructor;
  private final Method method;

  private final Object type;

  @SneakyThrows
  public ActionBarUtil() {
    final Class<?> cmtClass;
    final Class<?> icbcClass = getNms("IChatBaseComponent");
    final Class<?> ppocClass = getNms("PacketPlayOutChat");

    if (isEqualsOrMoreRecent(12)) {
      cmtClass = getNms("ChatMessageType");
      type = cmtClass.getEnumConstants()[2];
    } else {
      cmtClass = byte.class;
      type = (byte) 2;
    }

    if (icbcClass.getDeclaredClasses().length > 0) {
      method = icbcClass.getDeclaredClasses()[0].getMethod("a", String.class);
    } else {
      method = getNms("ChatSerializer").getMethod("a", String.class);
    }

    if (isEqualsOrMoreRecent(16)) {
      constructor = ppocClass.getConstructor(icbcClass, cmtClass, UUID.class);
    } else {
      constructor = ppocClass.getConstructor(icbcClass, cmtClass);
    }
  }

  @SneakyThrows
  public final void send(final CommandSender sender, final String message) {
    if (!(sender instanceof Player)) {
      return;
    }

    final Player player = (Player) sender;
    if (isEqualsOrMoreRecent(16)) {
      final Object chatBase = method.invoke(null, "{\"text\":\"" + message + "\"}");
      final Object packet = constructor.newInstance(chatBase, type, player.getUniqueId());
      sendPacket(player, packet);
      return;
    }

    final Object chatBase = method.invoke(null, "{\"text\":\"" + message + "\"}");
    final Object packet = constructor.newInstance(chatBase, type);

    sendPacket(player, packet);
  }

  @SneakyThrows
  public final void sendAll(@NotNull final String message) {
    if (isEqualsOrMoreRecent(16)) {
      final Object chatBase = method.invoke(null, "{\"text\":\"" + message + "\"}");

      for (final Player player : Bukkit.getOnlinePlayers()) {
        final Object packet = constructor.newInstance(chatBase, type, player.getUniqueId());
        sendPacket(player, packet);
      }

      return;
    }

    final Object chatBase = method.invoke(null, "{\"text\":\"" + message + "\"}");
    final Object packet = constructor.newInstance(chatBase, type);

    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
  }

}

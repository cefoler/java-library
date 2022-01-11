package com.celeste.library.spigot.util;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.error.ServerStartError;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionNms {

  private static final String VERSION;
  private static final String PATH_NMS;
  private static final String PATH_OBC;

  private static final Method GET_HANDLE;
  private static final Method SEND_PACKET;

  private static final Field PLAYER_CONNECTION;

  static {
    VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    PATH_NMS = "net.minecraft.server." + VERSION + ".";
    PATH_OBC = "org.bukkit.craftbukkit." + VERSION + ".";

    try {
      final Class<?> craftPlayerClazz = getObc("entity.CraftPlayer");
      final Class<?> playerConnectionClazz = isEqualsOrMoreRecent(17)
        ? getNms("network.PlayerConnection")
        : getNms("PlayerConnection");

      final Class<?> packetClazz = isEqualsOrMoreRecent(17)
        ? getNmsUnversionated("network.protocol", "Packet")
        : getNms("Packet");

      final Class<?> entityPlayerClazz = isEqualsOrMoreRecent(17)
          ? getNms("level.EntityPlayer")
          : getNms("EntityPlayer");

      GET_HANDLE = Reflection.getMethod(craftPlayerClazz, "getHandle");
      SEND_PACKET = Reflection.getMethod(playerConnectionClazz, "sendPacket", packetClazz);

      PLAYER_CONNECTION = Reflection.getField(entityPlayerClazz, "playerConnection");
    } catch (Exception exception) {
      throw new ServerStartError(exception);
    }
  }

  public static void sendPacket(final Player player, final Object packet)
      throws InvocationTargetException, IllegalAccessException {
    final Object entityPlayer = Reflection.invoke(GET_HANDLE, player);
    final Object entityConnection = Reflection.get(PLAYER_CONNECTION, entityPlayer);

    Reflection.invoke(SEND_PACKET, entityConnection, packet);
  }

  /**
   * VERSIONATED PATH
   * @param nms Name of the NMS class
   * @return An NMS class
   * @throws ClassNotFoundException If class was not found
   */
  public static Class<?> getNms(final String nms) throws ClassNotFoundException {
    return Class.forName(PATH_NMS + nms);
  }

  /**
   * @param subPackage Subpackage path
   * @param className Class name
   * @return An NMS class
   * @throws ClassNotFoundException If class was not found
   */
  public static Class<?> getNmsUnversionated(final String subPackage, final String className) throws ClassNotFoundException {
    return Class.forName("net.minecraft." + subPackage + "." + className);
  }

  /**
   * @param obc Name of the OBC class
   * @return An OBC class
   * @throws ClassNotFoundException If class was not found
   */
  public static Class<?> getObc(final String obc) throws ClassNotFoundException {
    return Class.forName(PATH_OBC + obc);
  }

  /**
   * @param checkVersion Version number that will be checked EX: (16, 11, 8, 7)
   * @return If the version is the same
   */
  public static boolean isEquals(final int checkVersion) {
    return Integer.parseInt(VERSION.split("_")[1]) == checkVersion;
  }

  /**
   * @param checkVersion Version number that will be checked EX: (16, 11, 8, 7)
   * @return If the version is equal or more recent
   */
  public static boolean isEqualsOrMoreRecent(final int checkVersion) {
    return Integer.parseInt(VERSION.split("_")[1]) >= checkVersion;
  }

  /**
   * @param checkVersion Version number that will be checked EX: (16, 11, 8, 7)
   * @return If the version is equal or less recent
   */
  public static boolean isEqualsOrLessRecent(final int checkVersion) {
    return Integer.parseInt(VERSION.split("_")[1]) <= checkVersion;
  }

  public static int getVersion() {
    return Integer.parseInt(VERSION.split("_")[1]);
  }

}

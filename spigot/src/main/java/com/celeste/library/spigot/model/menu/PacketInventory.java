package com.celeste.library.spigot.model.menu;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.util.ReflectionNms;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class PacketInventory {

  private static String version;

  private static final Field iinvField;
  private static final Field titleField;
  private static final Field handleField;
  private static final Field containerCounterField;

  private static final Constructor openWindowPacketConstructor;

  private static final Field playerConnectionField;
  private static final Method sendPacket;

  static {
    String[] parts = Bukkit.class.getName().split(".");
    if (parts.length == 4)
      version = "";
    else
      version = "." + parts[4];
    Field tiinv;
    Field ttitle;
    Field thandle;
    Field tcontainerCounter;
    Constructor topenWindowPacket;
    Field tplayerConnection;
    Method tsendPacket;
    try {
      tiinv = getVersionedClass("org.bukkit.craftbukkit.inventory.CraftInventory")
          .getDeclaredField("inventory");
      tiinv.setAccessible(true);
      ttitle =
          getVersionedClass(
              "org.bukkit.craftbukkit.inventory.CraftInventoryCustom$MinecraftInventory")
              .getDeclaredField("title");
      ttitle.setAccessible(true);
      thandle = getVersionedClass("org.bukkit.craftbukkit.entity.CraftEntity")
          .getDeclaredField("handle");
      thandle.setAccessible(true);
      tcontainerCounter = getVersionedClass("net.minecraft.server.EntityPlayer")
          .getDeclaredField("containerCounter");
      tcontainerCounter.setAccessible(true);
      thandle = getVersionedClass("org.bukkit.craftbukkit.entity.CraftEntity")
          .getDeclaredField("handle");
      thandle.setAccessible(true);
      topenWindowPacket =
          getVersionedClass("net.minecraft.server.PacketPlayOutOpenWindow")
              .getDeclaredConstructor(int.class,
                  int.class, String.class, int.class, boolean.class);
      topenWindowPacket.setAccessible(true);
      tplayerConnection = getVersionedClass("net.minecraft.server.EntityPlayer")
          .getDeclaredField("playerConnection");
      tplayerConnection.setAccessible(true);
      tsendPacket = getVersionedClass("net.minecraft.server.PlayerConnection")
          .getDeclaredMethod("sendPacket",
              topenWindowPacket.getDeclaringClass().getSuperclass());
      tsendPacket.setAccessible(true);
    } catch (Exception ex) // Any would do, regardless
    {
      throw new ExceptionInInitializerError(ex);
    }
    iinvField = tiinv;
    titleField = ttitle;
    handleField = thandle;
    containerCounterField = tcontainerCounter;
    openWindowPacketConstructor = topenWindowPacket;
    playerConnectionField = tplayerConnection;
    sendPacket = tsendPacket;
  }

  private static Class getVersionedClass(String className) throws ClassNotFoundException {
    if (className.startsWith("net.minecraft.server"))
      if (version.isEmpty())
        return Class.forName(className);
      else
        return Class.forName(String
            .format("net.minecraft.server%s.%s", version, className.substring(("net.minecraft" +
                ".server.").length())));
    else if (className.startsWith("org.bukkit.craftbukkit"))
      if (version.isEmpty())
        return Class.forName(className);
      else
        return Class.forName(
            String.format("net.minecraft.server%s.%s", version, className.substring(("org.bukkit" +
                ".craftbukkit.").length())));
    throw new IllegalArgumentException("Not a versioned class!");
  }

  @SneakyThrows
  public static void renameInventory(final Player player, final String title, final int size) {
    final Inventory inventory = player.getOpenInventory().getTopInventory();

    if (inventory == null) {
      return;
    }

    if (inventory.getType() != InventoryType.CHEST) {
      return;
    }

    Object iinv = iinvField.get(inventory);
    titleField.set(iinv, title);
    Object handle = handleField.get(player);
    Integer containerCounter = (Integer) containerCounterField.get(handle);
    Object playerConnection = playerConnectionField.get(handle);
    Object packet = openWindowPacketConstructor
        .newInstance(containerCounter, 0, title, size, false);
    sendPacket.invoke(playerConnection, packet);
  }
}

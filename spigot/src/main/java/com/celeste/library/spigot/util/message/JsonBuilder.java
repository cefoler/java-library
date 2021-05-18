package com.celeste.library.spigot.util.message;

import static com.celeste.library.spigot.util.ReflectionNms.getNMS;
import static com.celeste.library.spigot.util.ReflectionNms.getOBC;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
@Builder
public class JsonBuilder {

  private static String text;
  private Class<?> cp;
  private Constructor<?> ppocc;
  private Method a, getHandle, sendPacket;
  private Field pcf;
  private String json = "{\"text\":\"" + text + "\"}";
  private String hover;
  private String click;
  private HoverEventType hoverAction;
  private ClickEventType clickAction;
  public JsonBuilder() {
    try {
      final Class<?> cbc = getNMS("IChatBaseComponent");
      final Class<?> ppoc = getNMS("PacketPlayOutChat");

      this.cp = getOBC("CraftPlayer");
      final Class<?> entityPlayer = getNMS("EntityPlayer");
      final Class<?> playerConnection = getNMS("PlayerConnection");

      this.ppocc = ppoc.getConstructor(cbc, byte.class);

      this.a = getNMS("IChatBaseComponent$ChatSerializer").getMethod("a", String.class);
      this.getHandle = cp.getMethod("getHandle");
      this.sendPacket = playerConnection.getMethod("sendPacket", getNMS("Packet"));

      pcf = entityPlayer.getDeclaredField("playerConnection");
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  public JsonBuilder build() {
    if (getClick() == null && getHover() == null) {
      json = "{\"text\":\"" + text + "\"}";
      return this;
    }

    if (getClick() == null && getHover() != null) {
      json = "{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverAction.getName()
          + "\",\"value\":\"" + hover + "\"}}";
      return this;
    }
    if (getClick() != null && getHover() != null) {
      json = "{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"" + clickAction.getName()
          + "\",\"value\":\"" + click + "\"},\"hoverEvent\":{\"action\":\"" + hoverAction.getName()
          + "\",\"value\":\"" + hover + "\"}}";
      return this;
    }

    if (getClick() != null && getHover() == null) {
      json = "{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"" + clickAction.getName()
          + "\",\"value\":\"" + click + "\"}}";
      return this;
    }

    return this;
  }

  public void send(final Player player) {
    try {
      final Object messageComponent = a.invoke(null, json);
      final Object packet = ppocc.newInstance(messageComponent, (byte) 1);
      final Object craftPlayer = cp.cast(player);
      final Object entityPlayer = getHandle.invoke(craftPlayer);
      final Object playerConnection = pcf.get(entityPlayer);

      sendPacket.invoke(playerConnection, packet);
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  @Getter
  @AllArgsConstructor
  public enum ClickEventType {

    OPEN_URL("open_url"),
    RUN_COMMAND("run_command"),
    SUGGEST_TEXT("suggest_command");

    private final String name;
  }

  @Getter
  @AllArgsConstructor
  public enum HoverEventType {

    SHOW_TEXT("show_text"),
    SHOW_ITEM("show_item");

    private final String name;
  }

}

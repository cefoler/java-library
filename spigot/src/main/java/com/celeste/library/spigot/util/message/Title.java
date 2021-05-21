package com.celeste.library.spigot.util.message;

import static com.celeste.library.spigot.util.ReflectionNms.getNms;
import static com.celeste.library.spigot.util.ReflectionNms.sendPacket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public final class Title {

  public static final Title INSTANCE = new Title();

  private final Constructor<?> ppotTimeCon;
  private final Constructor<?> ppotTextCon;

  private final Method method;

  private final Object time;
  private final Object typeTitle;
  private final Object typeSubTitle;

  @SneakyThrows
  public Title() {
    final Class<?> ppotClass = getNms("PacketPlayOutTitle");
    final Class<?> icbcClass = getNms("IChatBaseComponent");
    final Class<?> etaClass;

    if (ppotClass.getDeclaredClasses().length > 0) {
      etaClass = ppotClass.getDeclaredClasses()[0];
    } else {
      etaClass = getNms("EnumTitleAction");
    }

    if (icbcClass.getDeclaredClasses().length > 0) {
      method = icbcClass.getDeclaredClasses()[0].getMethod("a", String.class);
    } else {
      method = getNms("ChatSerializer").getMethod("a", String.class);
    }

    ppotTimeCon = ppotClass.getConstructor(etaClass, icbcClass, int.class, int.class, int.class);
    ppotTextCon = ppotClass.getConstructor(etaClass, icbcClass);

    time = etaClass.getField("TIMES").get(null);
    typeTitle = etaClass.getField("TITLE").get(null);
    typeSubTitle = etaClass.getField("SUBTITLE").get(null);
  }

  @SneakyThrows
  public final void send(final Player player, final String title, final String subTitle) {
    final Object timePacket = ppotTimeCon.newInstance(time, null, 10, 10, 10);
    sendPacket(player, timePacket);

    final Object titleChatBase = method.invoke(null, "{\"text\":\"" + title + "\"}");
    final Object titlePacket = ppotTextCon.newInstance(typeTitle, titleChatBase);
    sendPacket(player, titlePacket);

    final Object subChatBase = method.invoke(null, "{\"text\":\"" + subTitle + "\"}");
    final Object subPacket = ppotTextCon.newInstance(typeSubTitle, subChatBase);
    sendPacket(player, subPacket);

  }

  @SneakyThrows
  public final void sendAll(final String title, final String subTitle) {
    final Object timePacket = ppotTimeCon.newInstance(time, null, 10, 10, 10);
    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, timePacket));

    final Object titleChatBase = method.invoke(null, "{\"text\":\"" + title + "\"}");
    final Object titlePacket = ppotTextCon.newInstance(typeTitle, titleChatBase);
    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, titlePacket));

    final Object chatBase = method.invoke(null, "{\"text\":\"" + subTitle + "\"}");
    final Object packet = ppotTextCon.newInstance(typeSubTitle, chatBase);
    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
  }

}

package com.celeste.library.spigot.util.message;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.celeste.library.spigot.util.ReflectionUtil.getNMS;
import static com.celeste.library.spigot.util.ReflectionUtil.sendPacket;

@Getter
public final class TitleUtil {

  public static final TitleUtil INSTANCE = new TitleUtil();

  private final Constructor<?> ppotTimeCon;
  private final Constructor<?> ppotTextCon;

  private final Method method;

  private final Object time;
  private final Object typeTitle;
  private final Object typeSubTitle;

  @SneakyThrows
  public TitleUtil() {
    final Class<?> ppotClass = getNMS("PacketPlayOutTitle");
    final Class<?> icbcClass = getNMS("IChatBaseComponent");
    final Class<?> etaClass;

    if (ppotClass.getDeclaredClasses().length > 0) {
      etaClass = ppotClass.getDeclaredClasses()[0];
    } else {
      etaClass = getNMS("EnumTitleAction");
    }

    if (icbcClass.getDeclaredClasses().length > 0) {
      method = icbcClass.getDeclaredClasses()[0].getMethod("a", String.class);
    } else {
      method = getNMS("ChatSerializer").getMethod("a", String.class);
    }

    ppotTimeCon = ppotClass.getConstructor(etaClass, icbcClass, int.class, int.class, int.class);
    ppotTextCon = ppotClass.getConstructor(etaClass, icbcClass);

    time = etaClass.getField("TIMES").get(null);
    typeTitle = etaClass.getField("TITLE").get(null);
    typeSubTitle = etaClass.getField("SUBTITLE").get(null);
  }

  @SneakyThrows
  public final void send(final CommandSender sender, @NotNull final String title, @NotNull final String subTitle) {
    if (!(sender instanceof Player)) return;

    final Player player = (Player) sender;

    final Object timePacket = ppotTimeCon.newInstance(time, null, 10, 10, 10);
    sendPacket(player, timePacket);

    final Object titleChatBase = method.invoke(null, "{\"text\":\"" + title + "\"}");
    final Object titlePacket = ppotTextCon.newInstance(typeTitle, titleChatBase);
    sendPacket(player, titlePacket);

    final Object subChatBase = method.invoke(null,"{\"text\":\"" + subTitle + "\"}");
    final Object subPacket = ppotTextCon.newInstance(typeSubTitle, subChatBase);
    sendPacket(player, subPacket);

  }

  @SneakyThrows
  public final void sendAll(@NotNull final String title, @NotNull final String subTitle) {
    final Object timePacket = ppotTimeCon.newInstance(time, null, 10, 10, 10);
    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, timePacket));

    final Object titleChatBase = method.invoke(null, "{\"text\":\"" + title + "\"}");
    final Object titlePacket = ppotTextCon.newInstance(typeTitle, titleChatBase);
    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, titlePacket));

    final Object chatBase = method.invoke(null,"{\"text\":\"" + subTitle + "\"}");
    final Object packet = ppotTextCon.newInstance(typeSubTitle, chatBase);
    Bukkit.getOnlinePlayers().forEach(player -> sendPacket(player, packet));
  }

}

package com.celeste.library.spigot.model.menu.entity;

import com.celeste.library.spigot.model.menu.AbstractMenu;
import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.model.menu.MenuItem;
import java.util.Properties;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public final class Context<T extends Event> {

  private final Player player;
  private final MenuHolder holder;
  private final T event;

  public UUID getPlayerId() {
    return player.getUniqueId();
  }

  public String getPlayerName() {
    return player.getName();
  }

  public void sendMessage(final String message) {
    player.sendMessage(message.replace("&", "§"));
  }

  public void close() {
    player.closeInventory();
  }

  public int getPage() {
    return holder.getProperty("page");
  }

  public Properties getProperties() {
    return holder.getProperties();
  }

  public MenuItem slot(final int slot, final ItemStack item) {
    return holder.slot(slot, item);
  }

  public void show(final AbstractMenu menu) {
    holder.show(menu, new Properties(), player);
  }

  public void show(final AbstractMenu menu, final int page) {
    holder.show(menu, page, player);
  }

  public void show(final AbstractMenu menu, final Properties properties) {
    holder.show(menu, properties, player);
  }

  public void reopen() {
    holder.reopen();
  }

  public void update(final Player player) {
    holder.update(player);
  }

}

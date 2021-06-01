package com.celeste.library.spigot.model.menu.entity;

import com.celeste.library.spigot.model.menu.AbstractMenu;
import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.model.menu.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.Properties;
import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class AbstractContext<T extends Event> implements Context<T> {

  private final Player player;
  private final T context;
  private final MenuHolder holder;

  public UUID getPlayerId() {
    return player.getUniqueId();
  }

  public Properties getProperties() {
    return holder.getProperties();
  }

  public void close() {
    player.closeInventory();
  }

  public void sendMessage(String message) {
    message = message.replace("&", "§");
    player.sendMessage(message);
  }

  public void show(final AbstractMenu menu) {
    holder.show(menu, player);
  }

  public MenuItem slot(final int slot, final ItemStack item) {
    return holder.slot(slot, item);
  }

}

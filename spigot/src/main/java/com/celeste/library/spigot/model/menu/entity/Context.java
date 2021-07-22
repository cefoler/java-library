package com.celeste.library.spigot.model.menu.entity;

import com.celeste.library.core.util.builder.DataBuilder;
import com.celeste.library.spigot.model.menu.AbstractMenu;
import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.model.menu.MenuItem;
import java.util.Properties;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
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

  public void message(final String message) {
    player.sendMessage(message.replace("&", "§"));
  }

  public void sound(final Sound sound, float volume, float pitch) {
    player.playSound(player.getLocation(), sound, volume, pitch);
  }

  public int getPage() {
    return getProperty("page");
  }

  public void setPage(int page) {
    if (page < 0) {
      page = 0;
    }

    setProperty("page", page);
  }

  public DataBuilder getProperties() {
    return DataBuilder.create(holder.getProperties());
  }

  public <P> P getProperty(final String key) {
    return holder.getProperty(key);
  }

  public void setProperty(final String key, final Object value) {
    holder.setProperty(key, value);
  }

  public MenuItem slot(final int slot, final ItemStack item) {
    return holder.slot(slot, item);
  }

  public MenuItem slot(final MenuItem menuItem) {
    return holder.slot(menuItem);
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

  public void close() {
    player.closeInventory();
  }

}

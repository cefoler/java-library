package com.celeste.library.spigot.model.menu;

import com.celeste.library.spigot.model.menu.action.ClickAction;
import java.util.Properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * The MenuItem is the object where the Item, Slot and Action will be storage.
 *
 * <p>This class also has default methods to close,
 * reopen and set property after the item has been clicked.</p>
 */
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public final class MenuItem {

  private final int slot;

  private ItemStack item;
  private ClickAction action;

  public MenuItem(@NotNull final ItemStack item, @NotNull final ClickAction action) {
    this.slot = 0;
    this.item = item;
    this.action = action;
  }

  public MenuItem(@NotNull final ItemStack item) {
    this.slot = 0;
    this.item = item;
  }

  public MenuItem() {
    this.slot = 0;
  }

  /**
   * When clicked, opens a new AbstractMenu without properties
   *
   * @param menu AbstractMenu that will be opened
   * @return MenuItem
   */
  public MenuItem open(final AbstractMenu menu) {
    return open(menu, new Properties());
  }

  /**
   * When clicked, opens a new AbstractMenu with properties as page one
   *
   * @param menu AbstractMenu that will be opened
   * @return MenuItem
   */
  public MenuItem open(final AbstractMenu menu, final int page) {
    final Properties properties = new Properties();
    properties.put("page", page);

    return open(menu, properties);
  }

  /**
   * When clicked, opens a new AbstractMenu with properties
   *
   * @param menu AbstractMenu that will be opened
   * @param properties Immutable map of the properties.
   * @return MenuItem
   */
  public MenuItem open(final AbstractMenu menu, final Properties properties) {
    return action((holder, event) -> holder.show(menu, properties, (Player) event.getWhoClicked()));
  }

  /**
   * When clicked, clears and updates the inventory
   *
   * @return MenuItem
   */
  public MenuItem reopen() {
    return action((holder, event) -> holder.reopen());
  }

  /**
   * When clicked, clears and updates the inventory
   *
   * @return MenuItem
   */
  public MenuItem update() {
    return action((holder, event) -> holder.update((Player) event.getWhoClicked()));
  }

  /**
   * When the item is clicked, the menu is closed.
   *
   * @return MenuItem
   */
  public MenuItem close() {
    return action((holder, event) -> event.getWhoClicked().closeInventory());
  }

  /**
   * Sets the item of this MenuItem.
   *
   * @param item ItemStack
   * @return MenuItem
   */
  public MenuItem item(final ItemStack item) {
    this.item = item;
    return this;
  }

  /**
   * Executes the ClickAction provided
   *
   * @param clickAction ClickAction for the item.
   * @return MenuItem
   */
  public MenuItem action(final ClickAction clickAction) {
    this.action = action != null ? action.and(clickAction) : clickAction;
    return this;
  }

  public MenuItem cancel() {
    return action((holder, event) -> event.setCancelled(true));
  }

  public MenuItem message(final String... message) {
    return action(((holder, event) -> event.getWhoClicked().sendMessage(message)));
  }

  public MenuItem sound(final Sound sound, final float volume, final float pitch) {
    return action(((holder, event) -> {
      final Player player = (Player) event.getWhoClicked();
      player.playSound(player.getLocation(), sound, volume, pitch);
    }));
  }

  /**
   * After the item is clicked, it sets a property into the holder.
   *
   * @param key   Key
   * @param value Value for the key
   * @return MenuItem
   */
  public MenuItem setProperty(final String key, final Object value) {
    return action((holder, event) -> holder.setProperty(key, value));
  }

}

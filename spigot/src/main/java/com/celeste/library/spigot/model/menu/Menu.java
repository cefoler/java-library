package com.celeste.library.spigot.model.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public interface Menu {

  /**
   * Event when the menu is rendered
   *
   * @param player Player that will render the menu
   * @param holder MenuHolder
   */
  void onRender(final Player player, final MenuHolder holder);

  /**
   * Event when the menu is opened
   *
   * @param event InventoryOpenEvent for the AbstractMenu
   * @param holder MenuHolder
   */
  void onOpen(final InventoryOpenEvent event, final MenuHolder holder);

  /**
   * Event when the menu is opened
   *
   * @param event InventoryCloseEvent for the AbstractMenu
   * @param holder MenuHolder
   */
  void onClose(final InventoryCloseEvent event, final MenuHolder holder);

  /**
   * Event when the player drags item from their inventory
   *
   * @param event InventoryDragEvent for the AbstractMenu
   * @param holder MenuHolder
   */
  void onDrag(final InventoryDragEvent event, final MenuHolder holder);

}

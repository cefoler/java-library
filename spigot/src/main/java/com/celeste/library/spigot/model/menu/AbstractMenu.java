package com.celeste.library.spigot.model.menu;

import java.util.Properties;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

/**
 * The AbstractMenu class should be extended on the class that will create the AbstractMenu
 *
 * <p>When extended, the constructor will create a super with Title and Size
 * For modifications on the inventory, you should create the onRender, onOpen and onClose on the
 * class extended by AbstractMenu.</p>
 */
@Getter
public abstract class AbstractMenu {

  private final String title;
  private final int size;

  private final MenuItem[] items;

  /**
   * Creates the AbstractMenu with the title and size specified.
   *
   * @param title String
   * @param size  int
   */
  public AbstractMenu(final String title, final int size) {
    this.title = title;
    this.size = size;
    this.items = new MenuItem[size];
  }

  /**
   * Shows the player the AbstractMenu, this method doesn't contains Any properties.
   *
   * @param player The player that will open the AbstractMenu
   * @return MenuHolder
   */
  public final MenuHolder show(final Player player) {
    return show(player, new Properties());
  }

  /**
   * Shows the player the AbstractMenu.
   *
   * @param player     The player that will open the AbstractMenu
   * @param properties ImmutableMap of properties needed on the AbstractMenu class
   * @return MenuHolder
   */
  public final MenuHolder show(final Player player, final Properties properties) {
    final MenuHolder holder = new MenuHolder(this, properties);
    holder.show(player);

    return holder;
  }

  /**
   * Shows the player the AbstractMenu and provides the page for the properties.
   *
   * @param player The player that will open the AbstractMenu
   * @param page   Page number
   * @return MenuHolder
   */
  public final MenuHolder show(final Player player, final int page) {
    final Properties properties = new Properties();
    properties.put("page", page);

    final MenuHolder holder = new MenuHolder(this, properties);
    holder.show(player);

    return holder;
  }

  /**
   * Event when the menu is rendered
   *
   * @param player Player that will render the menu
   * @param holder MenuHolder
   */
  public void onRender(final Player player, final MenuHolder holder) {
  }

  /**
   * Event when the menu is opened
   *
   * @param event InventoryOpenEvent for the AbstractMenu
   * @param holder MenuHolder
   */
  public void onOpen(final InventoryOpenEvent event, final MenuHolder holder) {
  }

  /**
   * Event when the menu is opened
   *
   * @param event InventoryCloseEvent for the AbstractMenu
   * @param holder MenuHolder
   */
  public void onClose(final InventoryCloseEvent event, final MenuHolder holder) {
  }

  /**
   * Event when the player drags item from their inventory
   *
   * @param event InventoryDragEvent for the AbstractMenu
   * @param holder MenuHolder
   */
  public void onDrag(final InventoryDragEvent event, final MenuHolder holder) {
  }

}


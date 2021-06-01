package com.celeste.library.spigot.model.menu;

import java.util.Properties;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.exception.MenuException;
import com.celeste.library.spigot.model.menu.annotation.Menu;
import com.celeste.library.spigot.model.menu.entity.AbstractContext;
import com.celeste.library.spigot.model.menu.entity.event.InventoryRenderEvent;
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
   */
  public AbstractMenu() {
    final Class<? extends AbstractMenu> clazz = getClass();
    if (!Reflection.containsAnnotation(clazz, Menu.class)) {
      throw new MenuException("The menu " + clazz.getSimpleName() + " doesn't have a @Menu annotation");
    }

    final Menu annotation = Reflection.getAnnotation(clazz, Menu.class);

    this.title = annotation.title();
    this.size = annotation.size();
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

  public void onRender(final AbstractContext<InventoryRenderEvent> context) {
  }

  public void onOpen(final AbstractContext<InventoryOpenEvent> context) {
  }

  public void onClose(final AbstractContext<InventoryCloseEvent> context) {
  }

  public void onDrag(final AbstractContext<InventoryDragEvent> context) {
  }

  /**
   * Event when the menu is rendered
   *
   * @param player Player that will render the menu
   * @param holder MenuHolder
   *
   * @deprecated since 3.0.6
   */
  @Deprecated
  public void onRender(final Player player, final MenuHolder holder) {
  }

  /**
   * Event when the menu is opened
   *
   * @param event InventoryOpenEvent for the AbstractMenu
   * @param holder MenuHolder
   *
   * @deprecated since 3.0.6
   */
  @Deprecated
  public void onOpen(final InventoryOpenEvent event, final MenuHolder holder) {
  }

  /**
   * Event when the menu is opened
   *
   * @param event InventoryCloseEvent for the AbstractMenu
   * @param holder MenuHolder
   *
   * @deprecated since 3.0.6
   */
  @Deprecated
  public void onClose(final InventoryCloseEvent event, final MenuHolder holder) {
  }

  /**
   * Event when the player drags item from their inventory
   *
   * @param event InventoryDragEvent for the AbstractMenu
   * @param holder MenuHolder
   *
   * @deprecated since 3.0.6
   */
  @Deprecated
  public void onDrag(final InventoryDragEvent event, final MenuHolder holder) {
  }

}


package com.celeste.library.spigot.model.menu;

import com.celeste.library.core.util.builder.DataBuilder;
import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.model.menu.annotation.Item;
import com.celeste.library.spigot.model.menu.annotation.Menu;
import com.celeste.library.spigot.model.menu.entity.Context;
import com.celeste.library.spigot.view.event.wrapper.impl.InventoryRenderEvent;

import java.lang.reflect.Method;
import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
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
@Setter
@Getter
public abstract class AbstractMenu {

  private String title;
  private int size;

  private MenuItem[] items;

  protected boolean cancelOnClick;

  /**
   * Creates the AbstractMenu with the title and size specified.
   */
  public AbstractMenu() {
    try {
      final Class<? extends AbstractMenu> clazz = getClass();

      if (Reflection.containsAnnotation(clazz, Menu.class)) {
        final Menu annotation = Reflection.getAnnotation(clazz, Menu.class);

        this.title = annotation.title();
        this.size = annotation.size();
        this.cancelOnClick = annotation.cancelOnClick();
      } else {
        this.title = " ";
        this.size = 54;
        this.cancelOnClick = false;
      }

      this.items = new MenuItem[size];

      for (final Method method : Reflection.getDcMethods(clazz)) {
        final Item annotation = method.getDeclaredAnnotation(Item.class);
        if (annotation == null || method.getReturnType() != MenuItem.class || method.getParameterCount() != 0) {
          continue;
        }

        final MenuItem item = Reflection.invoke(method, this);
        item.setSlot(annotation.slot());

        getItems()[annotation.slot()] = item;
      }
    } catch (ReflectiveOperationException exception) {
      exception.printStackTrace();
    }
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
   * Shows the player the AbstractMenu and provides the page for the properties.
   *
   * @param player The player that will open the AbstractMenu
   * @param page   Page number
   * @return MenuHolder
   */
  public final MenuHolder show(final Player player, final int page) {
    return show(player, DataBuilder.create().set("page", page));
  }

  /**
   * Shows the player the AbstractMenu.
   *
   * @param player The player that will open the AbstractMenu
   * @param properties ImmutableMap of properties needed on the AbstractMenu class
   * @return MenuHolder
   */
  public final MenuHolder show(final Player player, final Properties properties) {
    final MenuHolder holder = new MenuHolder(this, properties);
    holder.show(player);

    return holder;
  }

  /**
   * Event when the menu is rendered
   *
   * @param context Context
   */
  public void onRender(final Context<InventoryRenderEvent> context) {
  }

  /**
   * Event when the menu is opened
   *
   * @param context Context
   */
  public void onOpen(final Context<InventoryOpenEvent> context) {
  }

  /**
   * Event when the menu is opened
   *
   * @param context Context
   */
  public void onClose(final Context<InventoryCloseEvent> context) {
  }

  /**
   * Event when the player drags item from their inventory
   *
   * @param context Context
   */
  public void onDrag(final Context<InventoryDragEvent> context) {
  }

}


package com.celeste.library.spigot.model.menu;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.core.util.Validation;
import com.celeste.library.spigot.error.ServerStartError;
import com.celeste.library.spigot.exception.InvalidPropertyException;
import com.celeste.library.spigot.util.ReflectionNms;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

@Getter
public final class MenuHolder implements InventoryHolder {

  private static final Constructor<?> PACKET_WINDOW_CONSTRUCTOR;
  private static final Constructor<?> MESSAGE_CONSTRUCTOR;

  private static final Method GET_HANDLE;

  private static final Field ACTIVE_CONTAINER;
  private static final Field WINDOW_ID;

  static {
    try {
      final Class<?> packetWindowClazz = ReflectionNms.getNms("PacketPlayOutOpenWindow");
      final Class<?> componentClazz = ReflectionNms.getNms("IChatBaseComponent");
      final Class<?> messageClazz = ReflectionNms.getNms("ChatMessage");

      final Class<?> craftEntityClazz = ReflectionNms.getObc("entity.CraftEntity");
      final Class<?> entityPlayerClazz = ReflectionNms.getNms("EntityPlayer");
      final Class<?> containerClazz = ReflectionNms.getNms("Container");

      PACKET_WINDOW_CONSTRUCTOR = Reflection.getConstructor(packetWindowClazz, int.class,
          String.class, componentClazz, int.class);
      MESSAGE_CONSTRUCTOR = Reflection.getConstructor(messageClazz, String.class, Object[].class);

      GET_HANDLE = Reflection.getMethod(craftEntityClazz, "getHandle");

      ACTIVE_CONTAINER = Reflection.getField(entityPlayerClazz, "activeContainer");
      WINDOW_ID = Reflection.getField(containerClazz, "windowId");
    } catch (Exception exception) {
      throw new ServerStartError(exception);
    }
  }

  private final Properties properties;

  private AbstractMenu menu;
  private Inventory inventory;

  /**
   * AbstractMenu holder constructor.
   *
   * @param menu AbstractMenu
   * @param properties ImmutableMap of the properties.
   */
  public MenuHolder(final AbstractMenu menu, final Properties properties) {
    this.menu = menu;
    this.properties = properties;
  }

  /**
   * Creates the inventory and sets all items.
   *
   * @param player Player that will open the inventory
   */
  public void show(final Player player) {
    menu.onRender(player, this);

    this.inventory = Bukkit.createInventory(this, menu.getSize(), menu.getTitle());

    Arrays.stream(menu.getItems())
        .filter(item -> item != null && item.getItem() != null)
        .forEach(item -> inventory.setItem(item.getSlot(), item.getItem()));

    player.openInventory(inventory);
  }

  /**
   * Reopens the AbstractMenu provided with the items, title and slot without flicking (Via packets)
   */
  @SneakyThrows
  public void show(final AbstractMenu menu, final Player player) {
    inventory.clear();

    this.menu = menu;

    final Object entityPlayer = Reflection.invoke(GET_HANDLE, player);
    final Object container = Reflection.get(ACTIVE_CONTAINER, entityPlayer);

    final Object id = Reflection.get(WINDOW_ID, container);
    final Object newTitle = Reflection.instance(MESSAGE_CONSTRUCTOR, menu.getTitle(),
        new Object[0]);

    final Object packet = PACKET_WINDOW_CONSTRUCTOR.newInstance(id, "minecraft:chest",
        newTitle, menu.getSize());
    ReflectionNms.sendPacket(player, packet);

    menu.onRender(player, this);

    Arrays.stream(menu.getItems())
        .filter(item -> item != null && item.getItem() != null)
        .forEach(item -> inventory.setItem(item.getSlot(), item.getItem()));
  }

  /**
   * Reopens the AbstractMenu again with the new items set in the holder
   */
  public void reopen() {
    inventory.clear();

    Arrays.stream(menu.getItems())
        .filter(item -> item != null && item.getItem() != null)
        .forEach(item -> inventory.setItem(item.getSlot(), item.getItem()));
  }

  /**
   * Puts the item on the specific slot.
   *
   * @param item ItemStack
   * @param slot Slot
   * @return MenuItem
   */
  public MenuItem slot(final int slot, final ItemStack item) {
    final MenuItem menuItem = new MenuItem(slot).item(item);
    menu.getItems()[slot] = menuItem;

    return menuItem;
  }

  public void handleClick(final InventoryClickEvent event) {
    event.setCancelled(true);

    final int slot = event.getSlot();
    if (slot < 0) {
      return;
    }

    final MenuItem menuItem = menu.getItems()[slot];
    if (menuItem == null || menuItem.getAction() == null) {
      return;
    }

    menuItem.getAction().run(this, event);
  }

  public void handleOpen(final InventoryOpenEvent event) {
    menu.onOpen(event, this);
  }

  public void handleClose(final InventoryCloseEvent event) {
    menu.onClose(event, this);
  }

  public void handleDrag(final InventoryDragEvent event) {
    menu.onDrag(event, this);
  }

  /**
   * Gets the properties with that Key on the ImmutableMap.
   *
   * @param key Key to get the value
   * @param <T> Property class
   * @return Class of the property
   */
  public <T> T getProperty(final String key) {
    final Object object = properties.get(key);
    Validation.notNull(object, InvalidPropertyException.class);

    return (T) object;
  }

  /**
   * Sets the properties with that Key on the Properties.
   *
   * @param key   Key for the value
   * @param value Property object
   */
  public void setProperty(final String key, final Object value) {
    properties.put(key, value);
  }

  public void setProperties(final Properties newProperties) {
    properties.putAll(newProperties);
  }

  /**
   * Checks it the property exists.
   *
   * @param key Key to get the value
   * @return boolean If exists
   */
  public boolean hasProperty(final String key) {
    return properties.containsKey(key);
  }

}

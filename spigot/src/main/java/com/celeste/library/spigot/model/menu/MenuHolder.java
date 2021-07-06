package com.celeste.library.spigot.model.menu;

import com.celeste.library.core.model.entity.Data;
import com.celeste.library.core.util.Reflection;
import com.celeste.library.core.util.Validation;
import com.celeste.library.spigot.error.ServerStartError;
import com.celeste.library.spigot.exception.InvalidPropertyException;
import com.celeste.library.spigot.model.menu.entity.Context;
import com.celeste.library.spigot.util.ReflectionNms;
import com.celeste.library.spigot.view.event.wrapper.impl.InventoryRenderEvent;
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

  private Properties properties;

  private AbstractMenu menu;
  private Inventory inventory;

  /**
   * AbstractMenu holder constructor.
   *
   * @param menu AbstractMenu
   * @param properties ImmutableMap of the properties
   */
  public MenuHolder(final AbstractMenu menu, final Properties properties) {
    this.menu = menu;
    this.properties = properties;

    properties.putIfAbsent("page", 1);
  }

  /**
   * Creates the inventory and sets all items.
   *
   * @param player Player that will open the inventory
   */
  public void show(final Player player) {
    final int size = menu.getSize();
    final String title = menu.getTitle();

    this.inventory = Bukkit.createInventory(this, size, title);

    final InventoryRenderEvent event = new InventoryRenderEvent(player, inventory);
    Bukkit.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      return;
    }

    Arrays.stream(menu.getItems())
        .filter(item -> item != null && item.getItem() != null)
        .forEach(item -> inventory.setItem(item.getSlot(), item.getItem()));

    player.openInventory(inventory);
  }

  /**
   * Reopens the AbstractMenu provided with the items, title and slot without flicking (Via
   * packets).
   */
  public void show(final AbstractMenu menu, final int page, final Player player) {
    show(menu, Data.create().setData("page", page), player);
  }

  /**
   * Reopens the AbstractMenu provided with the items, title and slot without flicking (Via
   * packets).
   */
  @SneakyThrows
  public void show(final AbstractMenu menu, final Properties properties, final Player player) {
    this.menu = menu;
    this.properties = properties;

    inventory.clear();
    properties.putIfAbsent("page", 1);

    final ItemStack[] contents = player.getInventory().getContents();
    final ItemStack[] armor = player.getInventory().getArmorContents();
    player.getInventory().clear();

    final Object entityPlayer = Reflection.invoke(GET_HANDLE, player);
    final Object container = Reflection.get(ACTIVE_CONTAINER, entityPlayer);

    final Object id = Reflection.get(WINDOW_ID, container);
    final Object title = Reflection.instance(MESSAGE_CONSTRUCTOR, menu.getTitle(),
        new Object[0]);

    final Object packet = PACKET_WINDOW_CONSTRUCTOR.newInstance(id, "minecraft:chest",
        title, menu.getSize());
    ReflectionNms.sendPacket(player, packet);

    final InventoryRenderEvent event = new InventoryRenderEvent(player, inventory);
    Bukkit.getPluginManager().callEvent(event);

    player.getInventory().setContents(contents);
    player.getInventory().setArmorContents(armor);

    if (event.isCancelled()) {
      return;
    }

    Arrays.stream(menu.getItems())
        .filter(item -> item != null && item.getItem() != null)
        .forEach(item -> inventory.setItem(item.getSlot(), item.getItem()));
  }

  /**
   * Reopens the AbstractMenu again with the new items set in the holder.
   */
  public void reopen() {
    inventory.clear();

    Arrays.stream(menu.getItems())
        .filter(item -> item != null && item.getItem() != null)
        .forEach(item -> inventory.setItem(item.getSlot(), item.getItem()));
  }

  /**
   * Reopens the AbstractMenu again with the new items set in the holder.
   */
  public void update(final Player player) {
    inventory.clear();
    menu.setItems(new MenuItem[menu.getSize()]);

    final InventoryRenderEvent event = new InventoryRenderEvent(player, inventory);
    Bukkit.getPluginManager().callEvent(event);

    if (event.isCancelled()) {
      return;
    }

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

  /**
   * Puts the item on the specific slot.
   * @return MenuItem
   */
  public MenuItem slot(final MenuItem item) {
    menu.getItems()[item.getSlot()] = item;
    return item;
  }

  public void handleClick(final InventoryClickEvent event) {
    if (menu.cancelOnClick) {
      event.setCancelled(true);
    }

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

  public void handleRender(final InventoryRenderEvent event) {
    menu.onRender(new Context<>(event.getPlayer(), this, event));
  }

  public void handleOpen(final InventoryOpenEvent event) {
    menu.onOpen(new Context<>((Player) event.getPlayer(), this, event));
  }

  public void handleClose(final InventoryCloseEvent event) {
    menu.onClose(new Context<>((Player) event.getPlayer(), this, event));
  }

  public void handleDrag(final InventoryDragEvent event) {
    menu.onDrag(new Context<>((Player) event.getWhoClicked(), this, event));
  }

  /**
   * Gets the properties with that Key on the ImmutableMap.
   *
   * @param key Key to get the value
   * @param <T> Property class
   * @return Class of the property
   */
  @SuppressWarnings("unchecked")
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

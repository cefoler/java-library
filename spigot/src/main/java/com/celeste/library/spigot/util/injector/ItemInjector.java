package com.celeste.library.spigot.util.injector;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.error.ServerStartError;
import com.celeste.library.spigot.util.ReflectionNms;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public final class ItemInjector {

  private static final Constructor<?> COMPOUND_CONSTRUCTOR;

  private static final Constructor<?> STRING_CONSTRUCTOR;
  private static final Constructor<?> DOUBLE_CONSTRUCTOR;
  private static final Constructor<?> INT_CONSTRUCTOR;

  private static final Method AS_NMS_COPY;
  private static final Method HAS_TAG;
  private static final Method GET_TAG;

  private static final Method GET_STRING;
  private static final Method GET_DOUBLE;
  private static final Method GET_INT;

  private static final Method SET;
  private static final Method SET_TAG;
  private static final Method GET_ITEM_META;

  private static final Method REMOVE;

  private static final Method HAS_KEY;

  static {
    try {
      final Class<?> craftItemStackClazz = ReflectionNms.getObc("inventory.CraftItemStack");
      final Class<?> itemStackClazz = ReflectionNms.getNms("ItemStack");
      final Class<?> compoundClazz = ReflectionNms.getNms("NBTTagCompound");

      COMPOUND_CONSTRUCTOR = Reflection.getConstructor(compoundClazz);

      AS_NMS_COPY = Reflection.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
      HAS_TAG = Reflection.getMethod(itemStackClazz, "hasTag");
      GET_TAG = Reflection.getMethod(itemStackClazz, "getTag");

      // GET
      GET_STRING = Reflection.getMethod(compoundClazz, "getString", String.class);
      GET_DOUBLE = Reflection.getMethod(compoundClazz, "getInt", String.class);
      GET_INT = Reflection.getMethod(compoundClazz, "getDouble", String.class);

      // SET
      final Class<?> baseClazz = ReflectionNms.getNms("NBTBase");
      final Class<?> stringClazz = ReflectionNms.getNms("NBTTagString");
      final Class<?> doubleClazz = ReflectionNms.getNms("NBTTagDouble");
      final Class<?> intClazz = ReflectionNms.getNms("NBTTagInt");

      STRING_CONSTRUCTOR = Reflection.getConstructor(stringClazz, String.class);
      DOUBLE_CONSTRUCTOR = Reflection.getConstructor(doubleClazz, double.class);
      INT_CONSTRUCTOR = Reflection.getConstructor(intClazz, int.class);

      SET = Reflection.getMethod(compoundClazz, "set", String.class, baseClazz);
      SET_TAG = Reflection.getMethod(itemStackClazz, "setTag", compoundClazz);
      GET_ITEM_META = Reflection.getMethod(craftItemStackClazz, "getItemMeta", itemStackClazz);

      // REMOVE
      REMOVE = Reflection.getMethod(compoundClazz, "remove", String.class);

      // HAS
      HAS_KEY = Reflection.getMethod(compoundClazz, "hasKey", String.class);
    } catch (Exception exception) {
      throw new ServerStartError(exception);
    }
  }

  private final ItemStack item;

  public String getString(final String key) {
    final Object object = get(GET_STRING, key);
    return object != null ? (String) object : "";
  }

  public double getDouble(final String key) {
    final Object object = get(GET_DOUBLE, key);
    return object != null ? (double) object : 0;
  }

  public int getInteger(final String key) {
    final Object object = get(GET_INT, key);
    return object != null ? (int) object : 0;
  }

  @Nullable
  @SneakyThrows
  private Object get(final Method getNbtTag, final String key) {
    final Object nmsItem = Reflection.invokeStatic(AS_NMS_COPY, item);
    final boolean containsTag = Reflection.invoke(HAS_TAG, nmsItem);

    if (!containsTag) {
      return null;
    }

    final Object compound = Reflection.invoke(GET_TAG, nmsItem);
    return Reflection.invoke(getNbtTag, compound, key);
  }

  public void setString(final String key, final String value) {
    set(STRING_CONSTRUCTOR, key, value);
  }

  public void setDouble(final String key, final double value) {
    set(DOUBLE_CONSTRUCTOR, key, value);
  }

  public void setInteger(final String key, final int value) {
    set(INT_CONSTRUCTOR, key, value);
  }

  @SneakyThrows
  private void set(final Constructor<?> nbtTagConstructor, final String key, final Object value) {
    final Object nmsItemStack = Reflection.invokeStatic(AS_NMS_COPY, item);
    final boolean containsTag = Reflection.invoke(HAS_TAG, nmsItemStack);

    final Object compound = containsTag
        ? Reflection.invoke(GET_TAG, nmsItemStack)
        : Reflection.instance(COMPOUND_CONSTRUCTOR);

    final Object tag = Reflection.instance(nbtTagConstructor, value);
    Reflection.invoke(SET, compound, key, tag);
    Reflection.invoke(SET_TAG, nmsItemStack, compound);

    final ItemMeta meta = Reflection.invokeStatic(GET_ITEM_META, nmsItemStack);
    item.setItemMeta(meta);
  }

  @SneakyThrows
  public void remove(final String key) {
    final Object nmsItem = Reflection.invokeStatic(AS_NMS_COPY, item);
    final boolean containsTag = Reflection.invoke(HAS_TAG, nmsItem);

    if (!containsTag) {
      return;
    }

    final Object compound = Reflection.invoke(GET_TAG, nmsItem);
    Reflection.invoke(REMOVE, compound, key);

    final ItemMeta meta = Reflection.invokeStatic(GET_ITEM_META, nmsItem);
    item.setItemMeta(meta);
  }

  @SneakyThrows
  public boolean has(final String key) {
    final Object nmsItemStack = Reflection.invokeStatic(AS_NMS_COPY, item);
    final boolean containsTag = Reflection.invoke(HAS_TAG, nmsItemStack);

    if (!containsTag) {
      return false;
    }

    final Object compound = Reflection.invoke(GET_TAG, nmsItemStack);
    return Reflection.invoke(HAS_KEY, compound, key);
  }

  public ItemStack inject() {
    return item;
  }

}

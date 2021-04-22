package com.celeste.library.spigot.util.item;

import com.celeste.library.spigot.util.ReflectionUtil;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.celeste.library.spigot.util.ReflectionUtil.*;

@AllArgsConstructor
public final class ItemInjector {

  private final ItemStack item;

  private final Class<?> ctsClass;
  private final Class<?> isClass;
  private final Class<?> NTCClass;

  private final Method asNMSCopy;
  private final Method hasTag;
  private final Method getTag;
  private final Method getString;

  @SneakyThrows
  public ItemInjector(final ItemStack item) {
    this.item = item;

    ctsClass = getOBC("inventory.CraftItemStack");
    isClass = getNMS("ItemStack");
    NTCClass = getNMS("NBTTagCompound");

    asNMSCopy = getMethod(ctsClass, "asNMSCopy", ItemStack.class);
    hasTag = getMethod(isClass, "hasTag");
    getTag = getMethod(isClass, "getTag");
    getString = getMethod(NTCClass, "getString", String.class);
  }

  @SneakyThrows
  public boolean has(final String key) {
    final Object nmsItem = invokeStatic(asNMSCopy, item);
    final Object compound = invoke(getTag, nmsItem);
    final Object object = getString.invoke(compound, key);

    return object != null;
  }

  @SneakyThrows
  public String get(final String key) {
    final Object nmsItem = invokeStatic(asNMSCopy, item);
    final Object compound = invoke(getTag, nmsItem);

    return getString.invoke(compound, key).toString();
  }

  @SneakyThrows
  public void set(@NotNull final String key, @NotNull final Object value) {
    item.setItemMeta(item.getItemMeta());

    final Class<?> craftItemStackClazz = getOBC("inventory.CraftItemStack");
    final Class<?> itemStackClazz = getNMS("ItemStack");
    final Class<?> compoundClazz = getNMS("NBTTagCompound");

    final Method asNMSCopy = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
    final Method hasTag = ReflectionUtil.getMethod(itemStackClazz, "hasTag");
    final Method getTag = ReflectionUtil.getMethod(itemStackClazz, "getTag");

    final Object nmsItem = ReflectionUtil.invokeStatic(asNMSCopy, item);
    final boolean isExist = (Boolean) ReflectionUtil.invoke(hasTag, nmsItem);
    final Object compound = isExist ? ReflectionUtil.invoke(getTag, nmsItem) : compoundClazz.newInstance();

    final Class<?> tagClazz = getNMS("NBTTagString");
    final Class<?> baseClazz = getNMS("NBTBase");

    final Constructor<?> tagCon = ReflectionUtil.getDcConstructor(tagClazz, String.class);

    final Method set = ReflectionUtil.getMethod(compoundClazz, "set", String.class, baseClazz);
    final Method setTag = ReflectionUtil.getMethod(itemStackClazz, "setTag", compoundClazz);
    final Method getItemMeta = ReflectionUtil.getMethod(craftItemStackClazz, "getItemMeta", itemStackClazz);

    final Object tag = ReflectionUtil.instance(tagCon, value.toString());
    ReflectionUtil.invoke(set, compound, key, tag);
    ReflectionUtil.invoke(setTag, nmsItem, compound);

    item.setItemMeta((ItemMeta) ReflectionUtil.invokeStatic(getItemMeta, nmsItem));
  }

  public ItemStack inject() {
    return item;
  }

}

package com.celeste.library.spigot.util.item;

import static com.celeste.library.spigot.util.ReflectionNms.invoke;
import static com.celeste.library.spigot.util.ReflectionNms.invokeStatic;

import com.celeste.library.spigot.util.ReflectionNms;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@AllArgsConstructor
public final class ItemInjector {

  private final ItemStack item;

  private final Class<?> ctsClass;
  private final Class<?> isClass;
  private final Class<?> NtcClass;

  private final Method asNMSCopy;
  private final Method hasTag;
  private final Method getTag;
  private final Method getString;

  @SneakyThrows
  public ItemInjector(final ItemStack item) {
    this.item = item;

    ctsClass = ReflectionNms.getObc("inventory.CraftItemStack");
    isClass = ReflectionNms.getNms("ItemStack");
    NtcClass = ReflectionNms.getNms("NBTTagCompound");

    asNMSCopy = ReflectionNms.getMethod(ctsClass, "asNMSCopy", ItemStack.class);
    hasTag = ReflectionNms.getMethod(isClass, "hasTag");
    getTag = ReflectionNms.getMethod(isClass, "getTag");
    getString = ReflectionNms.getMethod(NtcClass, "getString", String.class);
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

    final Class<?> craftItemStackClazz = ReflectionNms.getObc("inventory.CraftItemStack");
    final Class<?> itemStackClazz = ReflectionNms.getNms("ItemStack");
    final Class<?> compoundClazz = ReflectionNms.getNms("NBTTagCompound");

    final Method asNMSCopy = ReflectionNms
        .getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
    final Method hasTag = ReflectionNms.getMethod(itemStackClazz, "hasTag");
    final Method getTag = ReflectionNms.getMethod(itemStackClazz, "getTag");

    final Object nmsItem = ReflectionNms.invokeStatic(asNMSCopy, item);
    final boolean isExist = (Boolean) ReflectionNms.invoke(hasTag, nmsItem);
    final Object compound =
        isExist ? ReflectionNms.invoke(getTag, nmsItem) : compoundClazz.newInstance();

    final Class<?> tagClazz = ReflectionNms.getNms("NBTTagString");
    final Class<?> baseClazz = ReflectionNms.getNms("NBTBase");

    final Constructor<?> tagCon = ReflectionNms.getDcConstructor(tagClazz, String.class);

    final Method set = ReflectionNms.getMethod(compoundClazz, "set", String.class, baseClazz);
    final Method setTag = ReflectionNms.getMethod(itemStackClazz, "setTag", compoundClazz);
    final Method getItemMeta = ReflectionNms
        .getMethod(craftItemStackClazz, "getItemMeta", itemStackClazz);

    final Object tag = ReflectionNms.instance(tagCon, value.toString());
    ReflectionNms.invoke(set, compound, key, tag);
    ReflectionNms.invoke(setTag, nmsItem, compound);

    item.setItemMeta((ItemMeta) ReflectionNms.invokeStatic(getItemMeta, nmsItem));
  }

  public ItemStack inject() {
    return item;
  }

}

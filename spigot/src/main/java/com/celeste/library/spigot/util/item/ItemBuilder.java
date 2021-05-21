package com.celeste.library.spigot.util.item;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.util.ReflectionNms;
import com.celeste.library.spigot.util.item.type.EnchantmentType;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ItemBuilder implements Cloneable {

  private final ItemStack itemStack;
  private ItemMeta meta;

  public ItemBuilder(final ItemStack itemStack) {
    this.itemStack = itemStack;
    this.meta = itemStack.getItemMeta();
  }

  public ItemBuilder(final Material material) {
    this(material, 1);
  }

  public ItemBuilder(final Material material, final int amount) {
    this.itemStack = new ItemStack(material, amount);
    this.meta = itemStack.getItemMeta();
  }

  public ItemBuilder(final Material material, final int amount, final int data) {
    this.itemStack = new ItemStack(material, amount, (short) data);
    this.meta = itemStack.getItemMeta();
  }

  public ItemBuilder material(final Material material) {
    itemStack.setType(material);
    return this;
  }

  public ItemBuilder data(final int data) {
    itemStack.setDurability((short) data);
    return this;
  }

  public ItemBuilder amount(final int amount) {
    itemStack.setAmount(amount);
    return this;
  }

  public ItemBuilder name(final String name) {
    meta.setDisplayName(name);
    return this;
  }

  public ItemBuilder setLore(final String... lore) {
    return setLore(ImmutableList.copyOf(lore));
  }

  public ItemBuilder setLore(final List<String> lore) {
    if (lore.size() == 0) {
      return this;
    }

    meta.setLore(lore);
    return this;
  }

  public ItemBuilder addLore(final String... lore) {
    return addLore(ImmutableList.copyOf(lore));
  }

  public ItemBuilder addLore(final List<String> lore) {
    if (lore.size() == 0) {
      return this;
    }

    final List<String> currentLore = meta.getLore();
    final List<String> newLore = currentLore == null
        ? new ArrayList<>()
        : currentLore;

    newLore.addAll(lore);
    meta.setLore(newLore);
    return this;
  }

  public ItemBuilder removeLore(final String... lore) {
    return removeLore(ImmutableList.copyOf(lore));
  }

  public ItemBuilder removeLore(final List<String> lore) {
    if (lore.size() == 0) {
      return this;
    }

    final List<String> currentLore = meta.getLore();
    final List<String> newLore = currentLore == null
        ? new ArrayList<>()
        : currentLore;

    newLore.removeAll(lore);
    meta.setLore(newLore);
    return this;
  }

  public ItemBuilder removeLoreLine(final int line) {
    final List<String> currentLore = meta.getLore();

    if (currentLore == null || line > currentLore.size()) {
      return this;
    }

    currentLore.remove(line);
    meta.setLore(currentLore);
    return this;
  }

  public ItemBuilder replaceLoreLine(final String lore, final int line) {
    final List<String> currentLore = meta.getLore();
    final List<String> newLore = currentLore == null
        ? new ArrayList<>()
        : currentLore;

    for (int index = newLore.size(); index <= line; index++) {
      newLore.add("§c");
    }

    newLore.set(line, lore);
    meta.setLore(newLore);
    return this;
  }

  public ItemBuilder addEnchantment(final String... enchantments) {
    return addEnchantment(ImmutableList.copyOf(enchantments));
  }

  public ItemBuilder addEnchantment(final List<String> enchantments) {
    if (enchantments.size() == 0) {
      return this;
    }

    enchantments.forEach(enchantment -> {
      final String[] split = enchantment.split(":");
      if (split.length != 2) {
        return;
      }

      final Enchantment enchant = EnchantmentType.getRealEnchantment(split[0]);
      final int level = Integer.parseInt(split[1]);
      itemStack.addUnsafeEnchantment(enchant, level);
    });

    return this;
  }

  public ItemBuilder addEnchantment(final String enchantment, final int level) {
    final Enchantment enchant = EnchantmentType.getRealEnchantment(enchantment);
    itemStack.addUnsafeEnchantment(enchant, level);
    return this;
  }

  @SafeVarargs
  public final ItemBuilder addEnchantment(final Entry<String, Integer>... enchantments) {
    if (enchantments.length == 0) {
      return this;
    }

    Arrays.stream(enchantments).forEach(enchantment -> {
      final Enchantment enchant = EnchantmentType.getRealEnchantment(enchantment.getKey());
      final int level = enchantment.getValue();
      itemStack.addUnsafeEnchantment(enchant, level);
    });

    return this;
  }

  public ItemBuilder addEnchantment(final Map<String, Integer> enchantments) {
    if (enchantments.size() == 0) {
      return this;
    }

    enchantments.forEach((enchantment, level) -> {
      final Enchantment enchant = EnchantmentType.getRealEnchantment(enchantment);
      itemStack.addUnsafeEnchantment(enchant, level);
    });

    return this;
  }

  public ItemBuilder removeEnchantment(final String... enchantments) {
    return removeEnchantment(ImmutableList.copyOf(enchantments));
  }

  public ItemBuilder removeEnchantment(final List<String> enchantments) {
    if (enchantments.size() == 0) {
      return this;
    }

    enchantments.forEach(enchantment -> {
      final Enchantment enchant = EnchantmentType.getRealEnchantment(enchantment);
      itemStack.removeEnchantment(enchant);
    });

    return this;
  }

  public ItemBuilder setDurability(final short durability) {
    itemStack.setDurability(durability);
    return this;
  }

  public ItemBuilder addDurability(final short durability) {
    final short currentDurability = itemStack.getDurability();
    final short newDurability = (short) (currentDurability + durability);

    itemStack.setDurability(newDurability);
    return this;
  }

  public ItemBuilder infinity() {
    itemStack.setDurability(Short.MAX_VALUE);
    meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
    return this;
  }

  public ItemBuilder glow(final boolean glow) {
    if (glow) {
      itemStack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
      meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    if (!itemStack.containsEnchantment(Enchantment.DURABILITY)) {
      return this;
    }

    itemStack.removeEnchantment(Enchantment.DURABILITY);
    meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
    return this;
  }

  @SneakyThrows
  public ItemBuilder skull(String texture, final UUID uuid) {
    final Material material = ReflectionNms.isEqualsOrMoreRecent(13) ?
        Enum.valueOf(Material.class, "PLAYER_HEAD") :
        Enum.valueOf(Material.class, "SKULL_ITEM");

    if (itemStack.getType() != material) {
      return this;
    }

    texture = "http://textures.minecraft.net/texture/" + texture;
    final SkullMeta skullMeta = (SkullMeta) meta;
    final Class<?> profileClass = Reflection.getClazz("com.mojang.authlib.GameProfile");
    final Class<?> propertyClass = Reflection.getClazz("com.mojang.authlib.properties.Property");

    final Constructor<?> profileCon = Reflection.getConstructor(profileClass, UUID.class, String.class);
    final Constructor<?> propertyCon = Reflection.getConstructor(propertyClass, String.class, String.class);
    final Field propertiesField = Reflection.getDcField(profileClass, "properties");

    final String encoded = Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", new Object[] { texture }).getBytes());
    final Object profile = Reflection.instance(profileCon, uuid, null);
    final Object property =Reflection. instance(propertyCon, "textures", encoded);

    final Class<?> propertiesClass = Reflection.getClazz(propertiesField);

    final Method put = Reflection.getMethod(propertiesClass,"put", Object.class, Object.class);
    Reflection.invoke(put, propertiesField.get(profile),"textures", property);

    final Field profileField = Reflection.getDcField(meta.getClass(), "profile");
    profileField.set(skullMeta, profile);
    return this;
  }

  public ItemBuilder skullOwner(final String owner) {
    final SkullMeta skullMeta = (SkullMeta) meta;

    skullMeta.setOwner(owner);
    return this;
  }

  public ItemBuilder mob(final EntityType type) {
    if (itemStack.getType() != Material.MOB_SPAWNER) return this;

    final BlockState state = ((BlockStateMeta) meta).getBlockState();
    ((CreatureSpawner) state).setSpawnedType(type);
    ((BlockStateMeta) meta).setBlockState(state);

    return this;
  }

  public ItemBuilder armor(final Color color) {
    final LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;

    armorMeta.setColor(color);
    return this;
  }

  public ItemBuilder potion(final List<String> potions) {
    if (itemStack.getType() != Material.POTION) {
      return this;
    }

    potions.forEach(potionString -> {
      final String[] split = potionString.split(":");

      if (split.length < 3) {
        return;
      }

      final String potionName = split[0];
      final int duration = Integer.parseInt(split[1]);
      final int amplifier = Integer.parseInt(split[2]);

      final PotionMeta potionMeta = (PotionMeta) meta;
      final PotionEffectType type = PotionEffectType.getByName(potionName);

      if (type == null) {
        return;
      }

      final PotionEffect effect = type.createEffect(duration * 20, amplifier);
      potionMeta.addCustomEffect(effect, true);

      final Potion potion = Potion.fromItemStack(itemStack);
      potion.setSplash(potion.isSplash());
      potion.apply(itemStack);
    });

    return this;
  }

  public ItemBuilder potion(final String potionName, final int duration, final int amplifier) {
    if (itemStack.getType() != Material.POTION) {
      return this;
    }

    final PotionMeta potionMeta = (PotionMeta) meta;
    final PotionEffectType type = PotionEffectType.getByName(potionName);

    if (type == null) {
      return this;
    }

    final PotionEffect effect = type.createEffect(duration * 20, amplifier);
    potionMeta.addCustomEffect(effect, true);

    final Potion potion = Potion.fromItemStack(itemStack);
    potion.setSplash(potion.isSplash());
    potion.apply(itemStack);
    return this;
  }

  public ItemBuilder removePotion(final String potionName) {
    if (itemStack.getType() != Material.POTION) return this;

    final PotionMeta potionMeta = (PotionMeta) meta;
    PotionEffectType type = PotionEffectType.getByName(potionName);

    if (!isValid(type) && Pattern.matches("[0-9]+", potionName)) {
      type = PotionEffectType.getById(Integer.parseInt(potionName));
    }

    if (type == null) return this;

    potionMeta.removeCustomEffect(type);

    final Potion potion = Potion.fromItemStack(itemStack);
    potion.setSplash(potion.isSplash());
    potion.apply(itemStack);

    return this;
  }

  public ItemBuilder clearPotion() {
    if (itemStack.getType() != Material.POTION) return this;

    final PotionMeta potionMeta = (PotionMeta) meta;
    potionMeta.clearCustomEffects();

    final Potion potion = Potion.fromItemStack(itemStack);
    potion.setSplash(potion.isSplash());
    potion.apply(itemStack);

    return this;
  }

  @SneakyThrows
  public <T> ItemBuilder nbtTag(final T key, final T value) {
    itemStack.setItemMeta(meta);

    final Class<?> craftItemStackClazz = getOBC("inventory.CraftItemStack");
    final Class<?> itemStackClazz = getNMS("ItemStack");
    final Class<?> compoundClazz = getNMS("NBTTagCompound");

    final Method asNMSCopy = getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
    final Method hasTag = getMethod(itemStackClazz, "hasTag");
    final Method getTag = getMethod(itemStackClazz, "getTag");

    final Object nmsItem = invokeStatic(asNMSCopy, itemStack);
    final boolean isExist = (Boolean) invoke(hasTag, nmsItem);
    final Object compound = isExist ? invoke(getTag, nmsItem) : compoundClazz.newInstance();

    final Class<?> tagClazz = getNMS("NBTTagString");
    final Class<?> baseClazz = getNMS("NBTBase");

    final Constructor<?> tagCon = getDcConstructor(tagClazz, String.class);

    final Method set = getMethod(compoundClazz, "set", String.class, baseClazz);
    final Method setTag = getMethod(itemStackClazz, "setTag", compoundClazz);
    final Method getItemMeta = getMethod(craftItemStackClazz, "getItemMeta", itemStackClazz);

    final Object tag = instance(tagCon, value.toString());
    invoke(set, compound, key.toString(), tag);
    invoke(setTag, nmsItem, compound);

    meta = (ItemMeta) invokeStatic(getItemMeta, nmsItem);

    return this;
  }

  public ItemBuilder itemFlag(final ItemFlag... flag) {
    meta.addItemFlags(flag);
    return this;
  }

  public ItemBuilder removeItemFlag(final ItemFlag... flag) {
    meta.removeItemFlags(flag);
    return this;
  }

  public ItemStack build() {
    itemStack.setItemMeta(meta);
    return itemStack;
  }

  private boolean isValid(final Object argument) {
    if (argument instanceof Object[]) return ((Object[]) argument).length != 0;

    if (argument instanceof List) return ((List<?>) argument).size() != 0;

    return argument != null && !argument.equals("");
  }

  @Override
  @SneakyThrows
  public ItemBuilder clone() {
    return (ItemBuilder) super.clone();
  }

}

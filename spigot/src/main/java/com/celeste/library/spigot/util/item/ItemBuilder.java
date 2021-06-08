package com.celeste.library.spigot.util.item;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.error.ServerStartError;
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
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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

  private static final Constructor<?> COMPOUND_CONSTRUCTOR;

  private static final Constructor<?> PROFILE_CONSTRUCTOR;
  private static final Constructor<?> PROPERTY_CONSTRUCTOR;

  private static final Method AS_NMS_COPY;
  private static final Method HAS_TAG;
  private static final Method GET_TAG;
  private static final Method SET_TAG;
  private static final Method GET_ITEM_META;

  private static final Method SET_CUSTOM_MODEL_DATA;

  private static final Method SET_BOOLEAN;

  private static final Method PUT;

  private static final Method SET_OWNER;

  private static final Field PROPERTIES;
  private static final Field PROFILE;

  private static final Material SKULL;

  private static final Material SPAWNER;

  static {
    try {
      final Class<?> craftItemStackClazz = ReflectionNms.getObc("inventory.CraftItemStack");
      final Class<?> itemStackClazz = ReflectionNms.getNms("ItemStack");
      final Class<?> compoundClazz = ReflectionNms.getNms("NBTTagCompound");

      COMPOUND_CONSTRUCTOR = Reflection.getConstructor(compoundClazz);

      AS_NMS_COPY = Reflection.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);
      HAS_TAG = Reflection.getMethod(itemStackClazz, "hasTag");
      GET_TAG = Reflection.getMethod(itemStackClazz, "getTag");
      SET_TAG = Reflection.getMethod(itemStackClazz, "setTag", compoundClazz);
      GET_ITEM_META = Reflection.getMethod(craftItemStackClazz, "getItemMeta", itemStackClazz);

      // MODEL DATA
      final Class<?> metaItemClazz = ReflectionNms.getObc("inventory.CraftMetaItem");
      SET_CUSTOM_MODEL_DATA = ReflectionNms.isEqualsOrMoreRecent(13)
          ? Reflection.getMethod(metaItemClazz, "setCustomModelData")
          : null;

      // UNBREAKABLE
      SET_BOOLEAN = Reflection.getMethod(compoundClazz, "setBoolean", String.class, boolean.class);

      // SKULL
      SKULL = ReflectionNms.isEqualsOrMoreRecent(13) ?
          Enum.valueOf(Material.class, "PLAYER_HEAD") :
          Enum.valueOf(Material.class, "SKULL_ITEM");

      final Class<?> profileClazz = Reflection.getClazz("com.mojang.authlib.GameProfile");
      final Class<?> propertyClazz = Reflection.getClazz("com.mojang.authlib.properties.Property");
      final Class<?> propertiesClazz = Reflection.getClazz("com.mojang.authlib.properties"
          + ".PropertyMap");
      final Class<?> metaSkullClazz = ReflectionNms.getObc("inventory.CraftMetaSkull");

      PROFILE_CONSTRUCTOR = Reflection.getConstructor(profileClazz, UUID.class, String.class);
      PROPERTY_CONSTRUCTOR = Reflection.getConstructor(propertyClazz, String.class, String.class);

      PUT = Reflection.getMethod(propertiesClazz, "put", Object.class, Object.class);

      PROPERTIES = Reflection.getDcField(profileClazz, "properties");
      PROFILE = Reflection.getDcField(metaSkullClazz, "profile");

      // SKULL OWNER
      SET_OWNER = ReflectionNms.isEqualsOrMoreRecent(13)
          ? Reflection.getMethod(metaSkullClazz, "setOwningPlayer", OfflinePlayer.class)
          : Reflection.getMethod(metaSkullClazz, "setOwner", String.class);

      // SPAWNERS
      SPAWNER = ReflectionNms.isEqualsOrMoreRecent(13) ?
          Enum.valueOf(Material.class, "SPAWNER") :
          Enum.valueOf(Material.class, "MOB_SPAWNER");
    } catch (Exception exception) {
      throw new ServerStartError(exception);
    }
  }

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

  @SuppressWarnings("deprecation")
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

  @SneakyThrows
  public ItemBuilder modelData(final int data) {
    SET_CUSTOM_MODEL_DATA.invoke(meta, data);
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

  public ItemBuilder lore(final String... lore) {
    return lore(ImmutableList.copyOf(lore));
  }

  public ItemBuilder lore(final List<String> lore) {
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

  @SuppressWarnings("deprecation")
  public ItemBuilder setDurability(final short durability) {
    itemStack.setDurability(durability);
    return this;
  }

  @SuppressWarnings("deprecation")
  public ItemBuilder addDurability(final short durability) {
    final short currentDurability = itemStack.getDurability();

    if (currentDurability == 0) {
      return this;
    }

    final short newDurability = (short) (currentDurability + durability);
    itemStack.setDurability(newDurability);
    return this;
  }

  @SneakyThrows
  public ItemBuilder unbreakable(final boolean unbreakable) {
    itemStack.setItemMeta(meta);

    final Object nmsItemStack = Reflection.invokeStatic(AS_NMS_COPY, itemStack);
    final boolean containsTag = (Boolean) Reflection.invoke(HAS_TAG, nmsItemStack);

    final Object compound = containsTag
        ? Reflection.invoke(GET_TAG, nmsItemStack)
        : Reflection.instance(COMPOUND_CONSTRUCTOR);

    Reflection.invoke(SET_BOOLEAN, compound, "Unbreakable", unbreakable);
    Reflection.invoke(SET_TAG, nmsItemStack, compound);

    this.meta = (ItemMeta) Reflection.invokeStatic(GET_ITEM_META, nmsItemStack);
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
  public ItemBuilder skull(final String texture, final UUID uuid) {
    if (itemStack.getType() != SKULL) {
      return this;
    }

    itemStack.setItemMeta(meta);

    final String newTexture = "https://textures.minecraft.net/texture/" + texture;
    final SkullMeta skullMeta = (SkullMeta) meta;

    final byte[] textureBytes = String.format("{textures:{SKIN:{url:\"%s\"}}}",
        new Object[]{newTexture}).getBytes();

    final String encoded = Base64.getEncoder().encodeToString(textureBytes);

    final Object profile = Reflection.instance(PROFILE_CONSTRUCTOR, uuid, null);
    final Object property = Reflection.instance(PROPERTY_CONSTRUCTOR, "textures", encoded);
    final Object properties = PROPERTIES.get(profile);

    Reflection.invoke(PUT, properties, "textures", property);
    PROFILE.set(skullMeta, profile);

    this.meta = skullMeta;
    return this;
  }

  @SneakyThrows
  @SuppressWarnings("deprecation")
  public ItemBuilder skullOwner(final String owner) {
    if (itemStack.getType() != SKULL) {
      return this;
    }

    final SkullMeta skullMeta = (SkullMeta) meta;
    final Object player = ReflectionNms.isEqualsOrMoreRecent(13)
        ? Bukkit.getOfflinePlayer(owner)
        : owner;

    Reflection.invoke(SET_OWNER, skullMeta, player);
    return this;
  }

  public ItemBuilder mob(final EntityType type) {
    if (itemStack.getType() != SPAWNER) {
      return this;
    }

    final BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
    final BlockState state = blockStateMeta.getBlockState();

    ((CreatureSpawner) state).setSpawnedType(type);
    blockStateMeta.setBlockState(state);
    return this;
  }

  public ItemBuilder armor(final Color color) {
    final LeatherArmorMeta armorMeta = (LeatherArmorMeta) meta;

    armorMeta.setColor(color);
    return this;
  }

  @SuppressWarnings("deprecation")
  public ItemBuilder addPotion(final List<String> potions) {
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

  @SuppressWarnings("deprecation")
  public ItemBuilder addPotion(final String potionName, final int duration, final int amplifier) {
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

  @SuppressWarnings("deprecation")
  public ItemBuilder removePotion(final String potionName) {
    if (itemStack.getType() != Material.POTION) {
      return this;
    }

    final PotionMeta potionMeta = (PotionMeta) meta;
    final PotionEffectType type = PotionEffectType.getByName(potionName);

    if (type == null) {
      return this;
    }

    potionMeta.removeCustomEffect(type);

    final Potion potion = Potion.fromItemStack(itemStack);
    potion.setSplash(potion.isSplash());
    potion.apply(itemStack);
    return this;
  }

  @SuppressWarnings("deprecation")
  public ItemBuilder clearPotion() {
    if (itemStack.getType() != Material.POTION) {
      return this;
    }

    final PotionMeta potionMeta = (PotionMeta) meta;
    potionMeta.clearCustomEffects();

    final Potion potion = Potion.fromItemStack(itemStack);
    potion.setSplash(potion.isSplash());
    potion.apply(itemStack);
    return this;
  }

  public ItemBuilder addItemFlag(final ItemFlag... flags) {
    meta.addItemFlags(flags);
    return this;
  }

  public ItemBuilder removeItemFlag(final ItemFlag... flags) {
    meta.removeItemFlags(flags);
    return this;
  }

  public ItemStack build() {
    itemStack.setItemMeta(meta);
    return itemStack;
  }

  @Override
  @SneakyThrows
  public ItemBuilder clone() {
    return (ItemBuilder) super.clone();
  }

}

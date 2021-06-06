package com.celeste.library.spigot.util.item;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.error.ServerStartError;
import com.celeste.library.spigot.util.ReflectionNms;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.Giant;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemGiantCreator {

  private static final Constructor<?> COMPOUND_CONSTRUCTOR;

  private static final Method GET_HANDLE;
  private static final Method GET_NBT_TAG;
  private static final Method C;
  private static final Method SET_INT;
  private static final Method F;

  private static final Method SET_INVISIBLE;

  static {
    try {
      final Class<?> entityClazz = ReflectionNms.getNms("Entity");

      // AI
      final Class<?> compoundClazz = ReflectionNms.getNms("NBTTagCompound");

      COMPOUND_CONSTRUCTOR = compoundClazz.getConstructor();

      GET_HANDLE = Reflection.getMethod(entityClazz, "getHandle");
      GET_NBT_TAG = Reflection.getMethod(entityClazz, "getNBTTag");
      C = Reflection.getMethod(entityClazz, "c", compoundClazz);
      SET_INT = Reflection.getMethod(entityClazz, "setInt", String.class, int.class);
      F = Reflection.getMethod(entityClazz, "setInt", compoundClazz);

      // INVISIBLE
      SET_INVISIBLE = Reflection.getMethod(entityClazz, "setInvisible", boolean.class);
    } catch (Exception exception) {
      throw new ServerStartError(exception);
    }
  }

  /**
   * Creates a Giant item
   *
   * @param location Location
   * @param item ItemStack
   */
  @SneakyThrows
  public static void create(final Location location, final ItemStack item) {
    final Giant giant = location.getWorld().spawn(location, Giant.class);
    final EntityEquipment equipment = giant.getEquipment();

    equipment.setItemInHand(item);
    equipment.setItemInHandDropChance(0);

    giant.setCustomNameVisible(false);
    giant.setCanPickupItems(false);
    giant.setRemoveWhenFarAway(false);

    final Object nmsEntity = Reflection.invoke(GET_HANDLE, giant);
    Object compound = Reflection.invoke(GET_NBT_TAG, nmsEntity);

    if (compound == null) {
      compound = Reflection.instance(COMPOUND_CONSTRUCTOR);
    }

    Reflection.invoke(C, nmsEntity, compound);
    Reflection.invoke(SET_INT, compound, "NoAI", 0);
    Reflection.invoke(F, nmsEntity, compound);

    Reflection.invoke(SET_INVISIBLE, nmsEntity, true);
  }

}

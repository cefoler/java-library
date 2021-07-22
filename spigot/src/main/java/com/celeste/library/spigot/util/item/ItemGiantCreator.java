package com.celeste.library.spigot.util.item;

import com.celeste.library.spigot.util.injector.EntityInjector;
import com.google.common.annotations.Beta;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.Giant;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

@Beta
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemGiantCreator {

  @SneakyThrows
  public static void create(final Location location, final ItemStack item) {
    final Giant giant = location.getWorld().spawn(location, Giant.class);
    final EntityEquipment equipment = giant.getEquipment();

    equipment.setItemInHand(item);
    equipment.setItemInHandDropChance(0);

    giant.setCustomNameVisible(false);
    giant.setCanPickupItems(false);
    giant.setRemoveWhenFarAway(false);

    final EntityInjector injector = new EntityInjector();

    injector.setAi(giant, false);
    injector.setInvisible(giant, true);
  }

}

package com.celeste.util.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.Giant;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GiantItemCreator {

  @Getter
  private static final GiantItemCreator instance = new GiantItemCreator();

  /**
   * Creates a Giant item
   *
   * @param location Location
   * @param item ItemStack
   */
  public void create(final Location location, final ItemStack item) {
    final Giant giant = location.getWorld().spawn(location, Giant.class);

    giant.getEquipment().setItemInHand(item);
    giant.setCustomNameVisible(false);
    giant.getEquipment().setItemInHandDropChance(0.0f);
    giant.setCanPickupItems(false);
    giant.setRemoveWhenFarAway(false);
    giant.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 999));
  }

}

package com.celeste.library.spigot.util.particles;

import com.celeste.library.spigot.util.particles.type.Particles;
import com.celeste.library.spigot.util.particles.type.Particles.BlockData;
import com.celeste.library.spigot.util.particles.type.Particles.ItemData;
import com.google.common.annotations.Beta;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Beta
public final class ParticlesCreator {

  private static final ThreadLocalRandom RANDOM;

  static {
    RANDOM = ThreadLocalRandom.current();
  }

  /**
   * Spawns a firework on the location
   * and sends him with the specified
   * details.
   *
   * @param location Base location
   * @param fireworkEffect Firework Effect shown
   * @param trail If trail can be seen
   * @param power Power of the firework
   * @param colors Colors to show
   *
   * @return Firework
   */
  public static Firework createFirework(final Location location, final FireworkEffect.Type fireworkEffect, final boolean flicker, final boolean trail, final int power, final Color... colors) {
    final Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
    final FireworkMeta fireworkMeta = firework.getFireworkMeta();

    final FireworkEffect effect = FireworkEffect.builder()
        .flicker(flicker)
        .withColor(colors)
        .withFade(colors)
        .with(fireworkEffect)
        .trail(trail)
        .build();

    fireworkMeta.addEffect(effect);
    fireworkMeta.setPower(power);

    firework.setFireworkMeta(fireworkMeta);
    return firework;
  }

  /**
   * Spawns the amount of items from
   * the materials provided and then it will
   * be sent in the another direction
   * creating a animation of explosion.
   *
   * @param multiply Speed that the item will travel
   * @param location Base location where the animation starts
   * @param amount Amount of items created
   * @param materials Materials that the items will be created
   *
   * @return Set of Items created
   */
  public static Set<Item> spawnItemsWithSpeed(final float multiply, final Location location, final int amount, final Material... materials) {
    final Set<Item> items = new HashSet<>();
    final World world = location.getWorld();

    for (int i = 0; i < amount; i++) {
      final Item item = world.dropItem(location.clone(), new ItemStack(materials[RANDOM.nextInt(materials.length)]));
      item.setPickupDelay(Integer.MAX_VALUE);

      final Vector vector = new Vector((RANDOM.nextBoolean() ? -1 : 0) + (RANDOM.nextDouble()), 1, (RANDOM.nextBoolean() ? -1 : 0) + (RANDOM.nextDouble()))
          .multiply(multiply)
          .normalize();

      item.setVelocity(vector);
      items.add(item);
    }

    return items;
  }

  public static Firework createFirework(final Location location, final FireworkEffect.Type fireworkEffect, final Color... colors) {
    return createFirework(location, fireworkEffect, false, true, 3, colors);
  }

  public static void display(final Particles effect, final Location location, final int amount, final float speed) {
    effect.display(0.0F, 0.0F, 0.0F, speed, amount, location, 128.0D);
  }

  public static void display(final Particles effect, final Location location, final int amount) {
    effect.display(0.0F, 0.0F, 0.0F, 0.0F, amount, location, 128.0D);
  }

  public static void display(final Particles effect, final Location location) {
    display(effect, location, 1);
  }

  public static void display(final Particles effect, final double x, final double y, final double z, final Location location, final int amount) {
    effect.display((float) x, (float) y, (float) z, 0.0F, amount, location, 128.0D);
  }

  public static void display(final Particles effect, final int red, final int green, final int blue, final Location location, final int amount) {
    for (int i = 0; i < amount; i++) {
      effect.display(new Particles.OrdinaryColor(red, green, blue), location, 128.0D);
    }
  }

  public static void display(final int red, final int green, final int blue, final Location location) {
    display(Particles.REDSTONE, red, green, blue, location, 1);
  }

  public static void display(final Particles effect, final int noteColor, final Location location) {
    effect.display(new Particles.NoteColor(noteColor), location, 128.0D);
  }

  public static void display(final Particles effect, final int red, final int green, final int blue, final Location location) {
    display(effect, red, green, blue, location, 1);
  }

  public static void display(final Particles effect, final ItemData itemData, final Location location) {
    effect.display(itemData, 0.0F, 0.0F, 0.0F, 0.0F, 1, location, 128.0D);
  }

  public static void display(final Particles effect, final ItemData itemData, final Location location, final int amount) {
    effect.display(itemData, 0.0F, 0.0F, 0.0F, 0.0F, amount, location, 128.0D);
  }

  public static void display(final Particles effect, final BlockData blockData, final Location location) {
    effect.display(blockData, 0.0F, 0.0F, 0.0F, 0.0F, 1, location, 128.0D);
  }

  public static void display(final Particles effect, final BlockData blockData, final Location location, final int amount) {
    effect.display(blockData, 0.0F, 0.0F, 0.0F, 0.0F, amount, location, 128.0D);
  }

}

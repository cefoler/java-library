package com.celeste.library.spigot.util.particles;

import com.celeste.library.spigot.util.particles.type.Particles;
import com.celeste.library.spigot.util.particles.type.Particles.BlockData;
import com.celeste.library.spigot.util.particles.type.Particles.ItemData;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public final class ParticlesCreator {

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

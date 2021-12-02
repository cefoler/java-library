package com.celeste.library.spigot.util.particles;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public final class ParticlesCreator {

  public Firework createFirework(final Location location, final FireworkEffect.Type fireworkEffect, final boolean flicker, final boolean trail, final int power, final Color... colors) {
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

  public Firework createFirework(final Location location, final FireworkEffect.Type fireworkEffect, final Color... colors) {
    return createFirework(location, fireworkEffect, false, true, 3, colors);
  }

}

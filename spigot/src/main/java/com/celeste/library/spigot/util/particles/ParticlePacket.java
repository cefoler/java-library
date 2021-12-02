package com.celeste.library.spigot.util.particles;

import com.celeste.library.core.util.Reflection;
import com.celeste.library.spigot.exception.particles.ParticlesException;
import com.celeste.library.spigot.util.ReflectionNms;
import com.celeste.library.spigot.util.particles.type.Particles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public final class ParticlePacket {

  private static final int VERSION;
  private static final boolean USE_SEND_PARTICLE;

  private static Class<?> ENUM_PARTICLE;
  private static Constructor<?> PACKET_CONSTRUCTOR;
  private static Method GET_HANDLE_METHOD;
  private static Field PLAYER_CONNECTION_FIELD;
  private static Method SEND_PACKET_METHOD;

  private final Particles effect;
  private float offsetX;
  private final float offsetY;
  private final float offsetZ;
  private final float speed;
  private final int amount;
  private final boolean longDistance;
  private final Particles.ParticleData data;
  private Object packet;

  static {
    try {
      final String serverVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);

      VERSION = Integer.parseInt(serverVersion.split("_")[1]);
      if (VERSION <= 7 || VERSION >= 13) {
        USE_SEND_PARTICLE = true;
      } else {
        ENUM_PARTICLE = ReflectionNms.getNms("EnumParticle");
        USE_SEND_PARTICLE = false;

        final Class<?> packetClass = ReflectionNms.getNms("PacketPlayOutWorldParticles");
        PACKET_CONSTRUCTOR = Reflection.getConstructor(packetClass);
        GET_HANDLE_METHOD = ReflectionNms.getObc("CraftPlayer").getMethod("getHandle");
        PLAYER_CONNECTION_FIELD = ReflectionNms.getNms("EntityPlayer").getField("playerConnection");
        SEND_PACKET_METHOD = Reflection.getMethod(PLAYER_CONNECTION_FIELD.getType(), "sendPacket", ReflectionNms.getNms("Packet"));
      }
    } catch (final Exception exception) {
      throw new ParticlesException("Your current Bukkit version seems to be incompatible with this library", exception);
    }
  }

  public ParticlePacket(final Particles effect, final float offsetX, final float offsetY, final float offsetZ,
                        final float speed, final int amount, final boolean longDistance, final Particles.ParticleData data) {
    if (speed < 0.0F) {
      throw new ParticlesException("The speed is lower than 0");
    }

    if (amount < 0) {
      throw new ParticlesException("The amount is lower than 0");
    }

    this.effect = effect;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.offsetZ = offsetZ;
    this.speed = speed;
    this.amount = amount;
    this.longDistance = longDistance;
    this.data = data;
  }

  public ParticlePacket(final Particles effect, final Vector direction, final float speed, final boolean longDistance, final Particles.ParticleData data) {
    this(effect, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0, longDistance, data);
  }

  public ParticlePacket(final Particles effect, final Particles.ParticleColor color, final boolean longDistance) {
    this(effect, color.getValueX(), color.getValueY(), color.getValueZ(), 1.0F, 0, longDistance, null);
    if (effect == Particles.REDSTONE && color instanceof Particles.OrdinaryColor && ((Particles.OrdinaryColor) color).getRed() == 0) {
      this.offsetX = 1.17549435E-38F;
    }
  }

  public void initializePacket(final Location center) {
    if (packet != null || USE_SEND_PARTICLE) {
      return;
    }

    try {
      this.packet = PACKET_CONSTRUCTOR.newInstance();
      if (VERSION < 8) {
        String name = effect.getName();
        if (data != null) {
          name = name + data.getPacketDataString();
        }

        Reflection.getDcField(packet, "a").set(packet, name);
      } else {
        Reflection.getDcField(packet, "a").set(packet, ENUM_PARTICLE.getEnumConstants()[this.effect.getId()]);
        Reflection.getDcField(packet, "j").set(packet, longDistance);

        if (this.data != null) {
          final int[] packetData = this.data.getPacketData();
          Reflection.getDcField(packet, "k").set(packet, this.effect == Particles.ITEM_CRACK ? packetData : new int[]{packetData[0] | packetData[1] << 12});
        }
      }

      Reflection.getDcField(packet, "b").set(packet, (float) center.getX());
      Reflection.getDcField(packet, "c").set(packet, (float) center.getY());
      Reflection.getDcField(packet, "d").set(packet, (float) center.getZ());
      Reflection.getDcField(packet, "e").set(packet, offsetX);
      Reflection.getDcField(packet, "f").set(packet, offsetY);
      Reflection.getDcField(packet, "g").set(packet, offsetZ);
      Reflection.getDcField(packet, "h").set(packet, speed);
      Reflection.getDcField(packet, "i").set(packet, amount);
    } catch (Exception exception) {
      throw new ParticlesException("Packet instantiation failed", exception);
    }
  }

  public void sendTo(final Location center, final Player player) {
    initializePacket(center);
    if (USE_SEND_PARTICLE) {
      return;
    }

    try {
      SEND_PACKET_METHOD.invoke(PLAYER_CONNECTION_FIELD.get(GET_HANDLE_METHOD.invoke(player)), this.packet);
    } catch (Exception exception) {
      throw new ParticlesException("Failed to send the packet to player '" + player.getName() + "'", exception);
    }
  }

  public void sendTo(final Location center, final List<Player> players) {
    if (USE_SEND_PARTICLE) {
      initializePacket(center);
    }

    for (final Player player : players) {
      sendTo(center, player);
    }
  }

  public void sendTo(final Location center, final double range) {
    if (USE_SEND_PARTICLE) {
      initializePacket(center);
      return;
    }

    if (range < 1.0D) {
      throw new IllegalArgumentException("The range is lower than 1");
    }

    final World worldName = center.getWorld();
    final double squared = range * range;

    Bukkit.getOnlinePlayers().forEach(player -> {
      if (player.getWorld() == worldName && !(player.getLocation().distanceSquared(center) > squared)) {
        this.sendTo(center, player);
      }
    });
  }

  public static int getVersion() {
    return VERSION;
  }

}

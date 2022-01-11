package com.celeste.library.spigot.util.particles.type;

import com.google.common.annotations.Beta;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.celeste.library.spigot.exception.particles.ParticlesException;
import com.celeste.library.spigot.util.particles.ParticlePacket;
import lombok.Data;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static com.celeste.library.spigot.util.particles.type.Particles.ParticleProperty.*;

@Beta
@Getter
public enum Particles {

  EXPLOSION_NORMAL("explode", 0, -1, DIRECTIONAL),
  EXPLOSION_LARGE("largeexplode", 1, -1),
  EXPLOSION_HUGE("hugeexplosion", 2, -1),
  FIREWORKS_SPARK("fireworksSpark", 3, -1, DIRECTIONAL),
  WATER_BUBBLE("bubble", 4, -1, DIRECTIONAL, REQUIRES_WATER),
  WATER_SPLASH("splash", 5, -1, DIRECTIONAL),
  WATER_WAKE("wake", 6, 7, DIRECTIONAL),
  SUSPENDED("suspended", 7, -1, REQUIRES_WATER),
  SUSPENDED_DEPTH("depthSuspend", 8, -1, DIRECTIONAL),
  CRIT("crit", 9, -1, DIRECTIONAL),
  CRIT_MAGIC("magicCrit", 10, -1, DIRECTIONAL),
  SMOKE_NORMAL("smoke", 11, -1, DIRECTIONAL),
  SMOKE_LARGE("largesmoke", 12, -1, DIRECTIONAL),
  SPELL("spell", 13, -1),
  SPELL_INSTANT("instantSpell", 14, -1),
  SPELL_MOB("mobSpell", 15, -1, COLORABLE),
  SPELL_MOB_AMBIENT("mobSpellAmbient", 16, -1, COLORABLE),
  SPELL_WITCH("witchMagic", 17, -1),
  DRIP_WATER("dripWater", 18, -1),
  DRIP_LAVA("dripLava", 19, -1),
  VILLAGER_ANGRY("angryVillager", 20, -1),
  VILLAGER_HAPPY("happyVillager", 21, -1, DIRECTIONAL),
  TOWN_AURA("townaura", 22, -1, DIRECTIONAL),
  NOTE("note", 23, -1, COLORABLE),
  PORTAL("portal", 24, -1, DIRECTIONAL),
  ENCHANTMENT_TABLE("enchantmenttable", 25, -1, DIRECTIONAL),
  FLAME("flame", 26, -1, DIRECTIONAL),
  LAVA("lava", 27, -1),
  CLOUD("cloud", 29, -1, DIRECTIONAL),
  REDSTONE("reddust", 30, -1, COLORABLE),
  SNOWBALL("snowballpoof", 31, -1),
  SNOW_SHOVEL("snowshovel", 32, -1, DIRECTIONAL),
  SLIME("slime", 33, -1),
  HEART("heart", 34, -1),
  BARRIER("barrier", 35, 8),
  ITEM_CRACK("iconcrack", 36, -1, DIRECTIONAL, REQUIRES_DATA),
  BLOCK_CRACK("blockcrack", 37, -1, REQUIRES_DATA),
  BLOCK_DUST("blockdust", 38, 7, DIRECTIONAL, REQUIRES_DATA),
  WATER_DROP("droplet", 39, 8),
  MOB_APPEARANCE("mobappearance", 41, 8);

  private static final Map<String, Particles> NAME_MAP;
  private static final Map<Integer, Particles> ID_MAP;

  static {
    NAME_MAP = new ConcurrentHashMap<>();
    ID_MAP = new ConcurrentHashMap<>();

    Arrays.asList(values()).forEach(effect -> {
      NAME_MAP.put(effect.getName(), effect);
      ID_MAP.put(effect.getId(), effect);
    });
  }

  private final String name;
  private final int id;
  private final int requiredVersion;
  private final List<ParticleProperty> properties;

  Particles(final String name, final int id, final int requiredVersion, final ParticleProperty... properties) {
    this.name = name;
    this.id = id;
    this.requiredVersion = requiredVersion;
    this.properties = Arrays.asList(properties);
  }

  public boolean hasProperty(final ParticleProperty property) {
    return properties.contains(property);
  }

  public boolean isSupported() {
    return requiredVersion == -1 || ParticlePacket.getVersion() >= requiredVersion;
  }

  public static Particles fromName(final String name, final Particles orElse) {
    return NAME_MAP.entrySet().stream()
        .filter(entry -> entry.getKey().equalsIgnoreCase(name))
        .map(Entry::getValue)
        .findFirst()
        .orElse(orElse);
  }

  public static Particles fromId(final int id, final Particles orElse) {
    return ID_MAP.entrySet().stream()
        .filter(entry -> entry.getKey() == id)
        .map(Entry::getValue)
        .findFirst()
        .orElse(orElse);
  }

  private static boolean isWater(final Location location) {
    final Material material = location.getBlock().getType();
    return material == Material.WATER || material == Material.getMaterial("STATIONARY_WATER");
  }

  private static boolean isLongDistance(final Location location, final List<Player> players) {
    final World world = location.getWorld();
    return players.stream()
        .anyMatch(player -> {
          final Location playerLocation = player.getLocation();
          return world == playerLocation.getWorld() || playerLocation.distanceSquared(location) > 65536.0D;
        });
  }

  private static boolean isCorrectData(final Particles effect, final ParticleData data) {
    return (effect == BLOCK_CRACK || effect == BLOCK_DUST) && data instanceof BlockData || effect == ITEM_CRACK && data instanceof ItemData;
  }

  private static boolean isCorrectColor(final Particles effect, final ParticleColor color) {
    return (effect == SPELL_MOB || effect == SPELL_MOB_AMBIENT || effect == REDSTONE) && color instanceof OrdinaryColor || effect == NOTE && color instanceof NoteColor;
  }

  public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (hasProperty(REQUIRES_DATA)) {
      throw new ParticlesException("This particle effect requires additional data");
    }

    if (hasProperty(REQUIRES_WATER) && !isWater(center)) {
      throw new IllegalArgumentException("There is no water at the center location");
    }

    new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256.0D, null)
        .sendTo(center, range);
  }

  public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (hasProperty(REQUIRES_DATA)) {
      throw new ParticlesException("This particle effect requires additional data");
    }

    if (hasProperty(REQUIRES_WATER) && !isWater(center)) {
      throw new IllegalArgumentException("There is no water at the center location");
    }

    new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), null)
        .sendTo(center, players);
  }

  @SneakyThrows
  public void display(final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Location center, final Player... players) {
    display(offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
  }

  public void display(final Vector direction, final float speed, final Location center, final double range) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (hasProperty(REQUIRES_DATA)) {
      throw new ParticlesException("This particle effect requires additional data");
    }

    if (!hasProperty(DIRECTIONAL)) {
      throw new IllegalArgumentException("This particle effect is not directional");
    }

    if (hasProperty(REQUIRES_WATER) && !isWater(center)) {
      throw new IllegalArgumentException("There is no water at the center location");
    }

    new ParticlePacket(this, direction, speed, range > 256.0D, null)
        .sendTo(center, range);
  }

  public void display(final Vector direction, final float speed, final Location center, final List<Player> players) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (hasProperty(REQUIRES_DATA)) {
      throw new ParticlesException("This particle effect requires additional data");
    }

    if (!hasProperty(DIRECTIONAL)) {
      throw new IllegalArgumentException("This particle effect is not directional");
    }

    if (hasProperty(REQUIRES_WATER) && !isWater(center)) {
      throw new IllegalArgumentException("There is no water at the center location");
    }

    new ParticlePacket(this, direction, speed, isLongDistance(center, players), null)
        .sendTo(center, players);
  }

  public void display(final Vector direction, final float speed, final Location center, final Player... players) {
    display(direction, speed, center, Arrays.asList(players));
  }

  public void display(final ParticleColor color, final Location center, final double range) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (!hasProperty(COLORABLE)) {
      throw new ParticlesException("This particle effect is not colorable");
    }

    if (!isCorrectColor(this, color)) {
      throw new ParticlesException("The particle color type is incorrect");
    }

    new ParticlePacket(this, color, range > 256.0D).sendTo(center, range);
  }

  public void display(final ParticleColor color, final Location center, final List<Player> players) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (!hasProperty(COLORABLE)) {
      throw new ParticlesException("This particle effect is not colorable");
    }

    if (!isCorrectColor(this, color)) {
      throw new ParticlesException("The particle color type is incorrect");
    }

    new ParticlePacket(this, color, isLongDistance(center, players)).sendTo(center, players);
  }

  public void display(final ParticleColor color, final Location center, final Player... players) {
    display(color, center, Arrays.asList(players));
  }

  public void display(final ParticleData data, final float offsetX, final float offsetY, final float offsetZ, final float speed, final int amount, final Location center, double range) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (!this.hasProperty(REQUIRES_DATA)) {
      throw new ParticlesException("This particle effect does not require additional data");
    }

    if (!isCorrectData(this, data)) {
      throw new ParticlesException("The particle data type is incorrect");
    }

    new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256.0D, data)
        .sendTo(center, range);
  }

  public void display(final ParticleData data, final float offsetX, final float offsetY, final float offsetZ,
                      final float speed, final int amount, final Location center, final List<Player> players) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (!hasProperty(REQUIRES_DATA)) {
      throw new ParticlesException("This particle effect does not require additional data");
    }

    if (!isCorrectData(this, data)) {
      throw new ParticlesException("The particle data type is incorrect");
    }

    new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), data).sendTo(center, players);
  }

  public void display(final ParticleData data, final float offsetX, final float offsetY,
                      final float offsetZ, final float speed, final int amount, final Location center, final Player... players) {
    display(data, offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
  }

  public void display(final ParticleData data, final Vector direction, final float speed, final Location center, final double range) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (!hasProperty(REQUIRES_DATA)) {
      throw new ParticlesException("This particle effect does not require additional data");
    }

    if (!isCorrectData(this, data)) {
      throw new ParticlesException("The particle data type is incorrect");
    }

    new ParticlePacket(this, direction, speed, range > 256.0D, data)
        .sendTo(center, range);
  }

  public void display(final ParticleData data, final Vector direction, final float speed, final Location center, final List<Player> players) {
    if (!isSupported()) {
      throw new ParticlesException("This particle effect is not supported by your server version");
    }

    if (!hasProperty(REQUIRES_DATA)) {
      throw new ParticlesException("This particle effect does not require additional data");
    }

    if (!isCorrectData(this, data)) {
      throw new ParticlesException("The particle data type is incorrect");
    }

    new ParticlePacket(this, direction, speed, isLongDistance(center, players), data).sendTo(center, players);
  }

  public void display(final ParticleData data, final Vector direction, final float speed, final Location center, final Player... players) {
    display(data, direction, speed, center, Arrays.asList(players));
  }

  public static final class NoteColor extends ParticleColor {

    private final int note;

    public NoteColor(final int note) {
      if (note < 0) {
        throw new IllegalArgumentException("The note value is lower than 0");
      }

      if (note > 24) {
        throw new IllegalArgumentException("The note value is higher than 24");
      }

      this.note = note;
    }

    public float getValueX() {
      return (float) this.note / 24.0F;
    }

    public float getValueY() {
      return 0.0F;
    }

    public float getValueZ() {
      return 0.0F;
    }
  }

  @Getter
  public static final class OrdinaryColor extends ParticleColor {

    private final int red;
    private final int green;
    private final int blue;

    public OrdinaryColor(final int red, final int green, final int blue) {
      this.red = Math.min(255, red);
      this.green = Math.min(255, green);
      this.blue = Math.min(255, blue);
    }

    public OrdinaryColor(final Color color) {
      this(color.getRed(), color.getGreen(), color.getBlue());
    }

    public float getValueX() {
      return (float) this.red / 255.0F;
    }

    public float getValueY() {
      return (float) this.green / 255.0F;
    }

    public float getValueZ() {
      return (float) this.blue / 255.0F;
    }

  }

  public abstract static class ParticleColor {

    public abstract float getValueX();

    public abstract float getValueY();

    public abstract float getValueZ();

  }

  public static final class BlockData extends ParticleData {
    public BlockData(final Material material, final byte data) {
      super(material, data);
      if (!material.isBlock()) {
        throw new IllegalArgumentException("The material is not a block");
      }
    }
  }

  public static final class ItemData extends ParticleData {
    public ItemData(final Material material, final byte data) {
      super(material, data);
    }
  }

  @Data @SuppressWarnings("deprecation")
  public abstract static class ParticleData {

    private final Material material;
    private final byte data;
    private final int[] packetData;

    public ParticleData(final Material material, final byte data) {
      this.material = material;
      this.data = data;
      this.packetData = new int[]{ material.getId(), data };
    }

    public String getPacketDataString() {
      return "_" + this.packetData[0] + "_" + this.packetData[1];
    }

  }

  public enum ParticleProperty {
    REQUIRES_WATER,
    REQUIRES_DATA,
    DIRECTIONAL,
    COLORABLE
  }

}

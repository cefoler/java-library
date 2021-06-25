package com.celeste.library.spigot.util;

import lombok.SneakyThrows;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Method;

public final class EntityInjector {

  private static Class<?> TAG_CLASS;

  static {
    try {
      TAG_CLASS = ReflectionNms.getNms("NBTTagCompound");
    } catch (ReflectiveOperationException exception) {
      exception.printStackTrace();
    }
  }

  @SneakyThrows
  public void setAI(final LivingEntity entity) {
    setInt(entity, "NoAI", 1);
  }

  @SneakyThrows
  public void set(final Method method, final Entity entity, final String key, Object value) {
    final Object handle = entity.getClass().getMethod("getHandle").invoke(entity);
    final Class<?> nmsEntity = handle.getClass();

    final Object tag = nmsEntity.getMethod("getNBTTag").invoke(handle) == null
        ? TAG_CLASS.getConstructor().newInstance()
        : nmsEntity.getMethod("getNBTTag").invoke(handle);

    nmsEntity.getMethod("c", TAG_CLASS).invoke(nmsEntity, tag);
    method.invoke(tag, key, value);
    nmsEntity.getMethod("f", TAG_CLASS).invoke(nmsEntity, tag);
  }

  @SneakyThrows
  public void setInt(final Entity entity, final String key, int value) {
    set(TAG_CLASS.getMethod("setInt", String.class, int.class), entity, key, value);
  }

  @SneakyThrows
  public void setString(final Entity entity, final String key, String value) {
    set(TAG_CLASS.getMethod("setString", String.class, String.class), entity, key, value);
  }

  @SneakyThrows
  public void setDouble(final Entity entity, final String key, double value) {
    set(TAG_CLASS.getMethod("setDouble", String.class, double.class), entity, key, value);
  }

  @SneakyThrows
  public int getInt(final Entity entity, final String key) {
    return (int) get("getInt", entity, key);
  }

  @SneakyThrows
  public String getString(final Entity entity, final String key) {
    return (String) get("getString", entity, key);
  }

  @SneakyThrows
  public double getDouble(final Entity entity, final String key) {
    return (double) get("getDouble", entity, key);
  }

  @SneakyThrows
  public Object get(final String method, final Entity entity, final String key) {
    final Object handle = entity.getClass().getMethod("getHandle").invoke(entity);
    final Class<?> nmsEntity = handle.getClass();

    final Object tag = nmsEntity.getMethod("getNBTTag").invoke(handle) == null
        ? TAG_CLASS.getConstructor().newInstance()
        : nmsEntity.getMethod("getNBTTag").invoke(handle);

    return TAG_CLASS.getMethod(method, String.class).invoke(tag, key);
  }

}

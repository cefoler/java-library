package com.celeste.library.spigot.util;

import com.celeste.library.core.util.Reflection;
import lombok.SneakyThrows;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.lang.reflect.Method;

public final class EntityInjector {

  private static Class<?> TAG_CLASS;
  private static Method GET_HANDLE;

  private static Method GET_NBT_TAG;

  private static Method GET_STRING;
  private static Method GET_INT;
  private static Method GET_DOUBLE;

  private static Method SET_STRING;
  private static Method SET_INT;
  private static Method SET_DOUBLE;

  private static Method C;
  private static Method F;

  public static Method SET_INVISIBLE;

  static {
    try {
      TAG_CLASS = ReflectionNms.getNms("NBTTagCompound");

      final Class<?> entityClazz = ReflectionNms.getNms("Entity");

      GET_HANDLE = Reflection.getMethod(entityClazz, "getHandle");
      GET_NBT_TAG = Reflection.getMethod(entityClazz, "getNBTTag");

      GET_STRING = entityClazz.getMethod("getString", String.class);
      GET_INT = entityClazz.getMethod("getInt", String.class);
      GET_DOUBLE = entityClazz.getMethod("getDouble", String.class);

      SET_STRING = entityClazz.getMethod("setString", String.class, String.class);
      SET_INT = entityClazz.getMethod("setInt", String.class, int.class);
      SET_DOUBLE = entityClazz.getMethod("setDouble", String.class, double.class);

      C = Reflection.getDcMethod(entityClazz, "c", TAG_CLASS);
      F = Reflection.getDcMethod(entityClazz, "f", TAG_CLASS);

      SET_INVISIBLE = Reflection.getMethod(entityClazz, "setInvisible", boolean.class);
    } catch (ReflectiveOperationException exception) {
      exception.printStackTrace();
    }
  }

  @SneakyThrows
  public void setAI(final LivingEntity entity, final boolean active) {
    setInt(entity, "NoAI", active ? 0 : 1);
  }

  @SneakyThrows
  public void setInvisible(final Entity entity, final boolean active) {
    Reflection.invoke(SET_INVISIBLE, entity, active);
  }

  @SneakyThrows
  public void set(final Method method, final Entity entity, final String key, Object value) {
    final Object handle = GET_HANDLE.invoke(entity);
    final Class<?> nmsEntity = handle.getClass();

    final Object tag = GET_NBT_TAG.invoke(handle) == null
        ? TAG_CLASS.getConstructor().newInstance()
        : GET_NBT_TAG.invoke(handle);

    C.invoke(nmsEntity, tag);
    method.invoke(tag, key, value);
    F.invoke(nmsEntity, tag);
  }

  @SneakyThrows
  public void setInt(final Entity entity, final String key, int value) {
    set(SET_INT, entity, key, value);
  }

  @SneakyThrows
  public void setString(final Entity entity, final String key, String value) {
    set(SET_STRING, entity, key, value);
  }

  @SneakyThrows
  public void setDouble(final Entity entity, final String key, double value) {
    set(SET_DOUBLE, entity, key, value);
  }

  @SneakyThrows
  public int getInt(final Entity entity, final String key) {
    return (int) get(GET_INT, entity, key);
  }

  @SneakyThrows
  public String getString(final Entity entity, final String key) {
    return (String) get(GET_STRING, entity, key);
  }

  @SneakyThrows
  public double getDouble(final Entity entity, final String key) {
    return (double) get(GET_DOUBLE, entity, key);
  }

  @SneakyThrows
  public Object get(final Method method, final Entity entity, final String key) {
    final Object handle = GET_HANDLE.invoke(entity);
    final Object tag = GET_NBT_TAG.invoke(handle) == null
        ? TAG_CLASS.getConstructor().newInstance()
        : GET_NBT_TAG.invoke(handle);

    return method.invoke(tag, key);
  }

}

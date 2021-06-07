package com.celeste.library.spigot.util;

import lombok.SneakyThrows;
import org.bukkit.entity.LivingEntity;

public final class EntityInjector {

  private static Class<?> TAG_CLASS;

  static {
    try {
      TAG_CLASS = ReflectionNms.getNms("NBTTagCompound");
    } catch (ClassNotFoundException exception) {
      exception.printStackTrace();
    }
  }

  @SneakyThrows
  public void setAI(final LivingEntity entity) {
    final Object handle = entity.getClass().getMethod("getHandle").invoke(entity);
    Object tag = handle.getClass().getMethod("getNBTTag").invoke(handle);

    if (tag == null) tag = TAG_CLASS.getConstructor().newInstance();

    handle.getClass().getMethod("c", TAG_CLASS).invoke(handle, tag);
    TAG_CLASS.getMethod("setInt", String.class, int.class).invoke(tag, "NoAI", 1);
    handle.getClass().getMethod("f", TAG_CLASS).invoke(handle, tag);
  }

}

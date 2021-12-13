package com.celeste.library.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Reflection {

  public static <T> Class<T> getClazz(final Object object) {
    final Class<?> clazz = object.getClass();
    return getClazz(clazz);
  }

  public static <T> Class<T> getClazz(final String path) throws ClassNotFoundException {
    final Class<?> clazz = Class.forName(path);
    return getClazz(clazz);
  }

  public static <T> Class<T> getClazz(final Field field) {
    final Class<?> clazz = field.getType();
    return getClazz(clazz);
  }

  @SuppressWarnings("unchecked")
  public static <T> Class<T> getClazz(final Class<?> clazz) {
    return (Class<T>) clazz;
  }

  public static Class<?>[] getClasses(final Object object) {
    final Class<?> clazz = getClazz(object);
    return getClasses(clazz);
  }

  public static Class<?> getClasses(final Object object, final int size) {
    final Class<?> clazz = getClazz(object);
    return getClasses(clazz, size);
  }

  public static Class<?>[] getClasses(final String path) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getClasses(clazz);
  }

  public static Class<?> getClasses(final String path, final int size)
      throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getClasses(clazz, size);
  }

  public static Class<?>[] getClasses(final Class<?> clazz) {
    return clazz.getClasses();
  }

  public static Class<?> getClasses(final Class<?> clazz, final int size) {
    return getClasses(clazz)[size];
  }

  public static Class<?>[] getDcClasses(final Object object) {
    final Class<?> clazz = getClazz(object);
    return getDcClasses(clazz);
  }

  public static Class<?> getDcClasses(final Object object, final int size) {
    final Class<?> clazz = getClazz(object);
    return getDcClasses(clazz, size);
  }

  public static Class<?>[] getDcClasses(final String path) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getDcClasses(clazz);
  }

  public static Class<?> getDcClasses(final String path, final int size)
      throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getDcClasses(clazz, size);
  }

  public static Class<?>[] getDcClasses(final Class<?> clazz) {
    return clazz.getDeclaredClasses();
  }

  public static Class<?> getDcClasses(final Class<?> clazz, final int size) {
    return getDcClasses(clazz)[size];
  }

  public static <T> Constructor<T> getConstructor(final Object object, final Class<?>... parameters)
      throws NoSuchMethodException {
    final Class<T> clazz = getClazz(object);
    return getConstructor(clazz, parameters);
  }

  public static <T> Constructor<T> getConstructor(final String path, final Class<?>... parameters)
      throws ClassNotFoundException, NoSuchMethodException {
    final Class<T> clazz = getClazz(path);
    return getConstructor(clazz, parameters);
  }

  public static <T> Constructor<T> getConstructor(final Class<T> clazz,
                                                  final Class<?>... parameters) throws NoSuchMethodException {
    return clazz.getConstructor(parameters);
  }

  public static <T> Constructor<T> getDcConstructor(final Object object,
                                                    final Class<?>... parameters) throws NoSuchMethodException {
    final Class<T> clazz = getClazz(object);
    return getDcConstructor(clazz, parameters);
  }

  public static <T> Constructor<T> getDcConstructor(final String path, final Class<?>... parameters)
      throws ClassNotFoundException, NoSuchMethodException {
    final Class<T> clazz = getClazz(path);
    return getDcConstructor(clazz, parameters);
  }

  public static <T> Constructor<T> getDcConstructor(final Class<T> clazz,
                                                    final Class<?>... parameters) throws NoSuchMethodException {
    final Constructor<T> constructor = clazz.getDeclaredConstructor(parameters);
    constructor.setAccessible(true);
    return constructor;
  }

  public static <T> Constructor<T>[] getConstructors(final Object object) {
    final Class<T> clazz = getClazz(object);
    return getConstructors(clazz);
  }

  public static <T> Constructor<T> getConstructors(final Object object, final int size) {
    final Class<T> clazz = getClazz(object);
    return getConstructors(clazz, size);
  }

  public static <T> Constructor<T>[] getConstructors(final String path)
      throws ClassNotFoundException {
    final Class<T> clazz = getClazz(path);
    return getConstructors(clazz);
  }

  public static <T> Constructor<T> getConstructors(final String path, final int size)
      throws ClassNotFoundException {
    final Class<T> clazz = getClazz(path);
    return getConstructors(clazz, size);
  }

  @SuppressWarnings("unchecked")
  public static <T> Constructor<T>[] getConstructors(final Class<T> clazz) {
    return (Constructor<T>[]) clazz.getConstructors();
  }

  public static <T> Constructor<T> getConstructors(final Class<T> clazz, final int size) {
    return getConstructors(clazz)[size];
  }

  public static <T> Constructor<T>[] getDcConstructors(final Object object) {
    final Class<T> clazz = getClazz(object);
    return getConstructors(clazz);
  }

  public static <T> Constructor<T> getDcConstructors(final Object object, final int size) {
    final Class<T> clazz = getClazz(object);
    return getConstructors(clazz, size);
  }

  public static <T> Constructor<T>[] getDcConstructors(final String path)
      throws ClassNotFoundException {
    final Class<T> clazz = getClazz(path);
    return getConstructors(clazz);
  }

  public static <T> Constructor<T> getDcConstructors(final String path, final int size)
      throws ClassNotFoundException {
    final Class<T> clazz = getClazz(path);
    return getConstructors(clazz, size);
  }

  @SuppressWarnings("unchecked")
  public static <T> Constructor<T>[] getDcConstructors(final Class<T> clazz) {
    return Arrays.stream(clazz.getDeclaredConstructors())
        .peek(constructor -> constructor.setAccessible(true))
        .toArray(Constructor[]::new);
  }

  @SuppressWarnings("unchecked")
  public static <T> Constructor<T> getDcConstructors(final Class<T> clazz, final int size) {
    final Constructor<T> constructor = (Constructor<T>) clazz.getDeclaredConstructors()[size];
    constructor.setAccessible(true);
    return constructor;
  }

  public static Method getMethod(final Object object, final String name,
                                 final Class<?>... parameters) throws NoSuchMethodException {
    final Class<?> clazz = getClazz(object);
    return getMethod(clazz, name, parameters);
  }

  public static Method getMethod(final String path, final String name, final Class<?>... parameters)
      throws ClassNotFoundException, NoSuchMethodException {
    final Class<?> clazz = getClazz(path);
    return getMethod(clazz, name, parameters);
  }

  public static Method getMethod(final Class<?> clazz, final String name,
                                 final Class<?>... parameters) throws NoSuchMethodException {
    return clazz.getMethod(name, parameters);
  }

  public static Method getDcMethod(final Object object, final String name,
                                   final Class<?>... parameters) throws NoSuchMethodException {
    final Class<?> clazz = getClazz(object);
    return getDcMethod(clazz, name, parameters);
  }

  public static Method getDcMethod(final String path, final String name,
                                   final Class<?>... parameters) throws ClassNotFoundException, NoSuchMethodException {
    final Class<?> clazz = getClazz(path);
    return getDcMethod(clazz, name, parameters);
  }

  public static Method getDcMethod(final Class<?> clazz, final String name,
                                   final Class<?>... parameters) throws NoSuchMethodException {
    final Method method = clazz.getDeclaredMethod(name, parameters);
    method.setAccessible(true);
    return method;
  }

  public static Method[] getMethods(final Object object) {
    final Class<?> clazz = getClazz(object);
    return getMethods(clazz);
  }

  public static Method getMethods(final Object object, final int size) {
    final Class<?> clazz = getClazz(object);
    return getMethods(clazz, size);
  }

  public static Method[] getMethods(final String path) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getMethods(clazz);
  }

  public static Method getMethods(final String path, final int size) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getMethods(clazz, size);
  }

  public static Method[] getMethods(final Class<?> clazz) {
    return clazz.getMethods();
  }

  public static Method getMethods(final Class<?> clazz, final int size) {
    return getMethods(clazz)[size];
  }

  public static Method[] getDcMethods(final Object object) {
    final Class<?> clazz = getClazz(object);
    return getDcMethods(clazz);
  }

  public static Method getDcMethods(final Object object, final int size) {
    final Class<?> clazz = getClazz(object);
    return getDcMethods(clazz, size);
  }

  public static Method[] getDcMethods(final String path) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getDcMethods(clazz);
  }

  public static Method getDcMethods(final String path, final int size)
      throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getDcMethods(clazz, size);
  }

  public static Method[] getDcMethods(final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .peek(method -> method.setAccessible(true))
        .toArray(Method[]::new);
  }

  public static Method getDcMethods(final Class<?> clazz, final int size) {
    final Method method = clazz.getDeclaredMethods()[size];
    method.setAccessible(true);
    return method;
  }

  public static Field getField(final Object object, final String name) throws NoSuchFieldException {
    final Class<?> clazz = getClazz(object);
    return getField(clazz, name);
  }

  public static Field getField(final String path, final String name)
      throws ClassNotFoundException, NoSuchFieldException {
    final Class<?> clazz = getClazz(path);
    return getField(clazz, name);
  }

  public static Field getField(final Class<?> clazz, final String name)
      throws NoSuchFieldException {
    return clazz.getField(name);
  }

  public static Field getDcField(final Object object, final String name)
      throws NoSuchFieldException {
    final Class<?> clazz = getClazz(object);
    return getDcField(clazz, name);
  }

  public static Field getDcField(final String path, final String name)
      throws ClassNotFoundException, NoSuchFieldException {
    final Class<?> clazz = getClazz(path);
    return getDcField(clazz, name);
  }

  public static Field getDcField(final Class<?> clazz, final String name)
      throws NoSuchFieldException {
    final Field field = clazz.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }

  public static Field[] getFields(final Object object) {
    final Class<?> clazz = getClazz(object);
    return getFields(clazz);
  }

  public static Field getFields(final Object object, final int size) {
    final Class<?> clazz = getClazz(object);
    return getFields(clazz, size);
  }

  public static Field[] getFields(final String path) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getFields(clazz);
  }

  public static Field getFields(final String path, final int size) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getFields(clazz, size);
  }

  public static Field[] getFields(final Class<?> clazz) {
    return clazz.getFields();
  }

  public static Field getFields(final Class<?> clazz, final int size) {
    return getFields(clazz)[size];
  }

  public static Field[] getDcFields(final Object object) {
    final Class<?> clazz = getClazz(object);
    return getDcFields(clazz);
  }

  public static Field getDcFields(final Object object, final int size) {
    final Class<?> clazz = getClazz(object);
    return getDcFields(clazz, size);
  }

  public static Field[] getDcFields(final String path) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getDcFields(clazz);
  }

  public static Field getDcFields(final String path, final int size) throws ClassNotFoundException {
    final Class<?> clazz = getClazz(path);
    return getDcFields(clazz, size);
  }

  public static Field[] getDcFields(final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .peek(field -> field.setAccessible(true))
        .toArray(Field[]::new);
  }

  public static Field getDcFields(final Class<?> clazz, final int size) {
    final Field field = clazz.getDeclaredFields()[size];
    field.setAccessible(true);
    return field;
  }

  public static <T extends Annotation> boolean containsAnnotation(final Class<?> clazz,
                                                                  final Class<T> annotation) {
    return clazz.isAnnotationPresent(annotation);
  }

  public static <T extends Annotation> boolean containsAnnotation(final Constructor<?> constructor,
                                                                  final Class<T> annotation) {
    return constructor.isAnnotationPresent(annotation);
  }

  public static <T extends Annotation> boolean containsAnnotation(final Field field,
                                                                  final Class<T> annotation) {
    return field.isAnnotationPresent(annotation);
  }

  public static <T extends Annotation> T getAnnotation(final Class<?> clazz,
                                                       final Class<T> annotation) {
    return clazz.getAnnotation(annotation);
  }

  public static <T extends Annotation> T getAnnotation(final Constructor<?> constructor,
                                                       final Class<T> annotation) {
    return constructor.getAnnotation(annotation);
  }

  public static <T extends Annotation> T getAnnotation(final Field field,
                                                       final Class<T> annotation) {
    return field.getAnnotation(annotation);
  }

  public static <T> T instance(final Object object)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
      InstantiationException {
    final Constructor<T> constructor = getConstructor(object);
    return instance(constructor);
  }

  public static <T> T instance(final Class<T> clazz)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
      InstantiationException {
    final Constructor<T> constructor = getConstructor(clazz);
    return instance(constructor);
  }

  public static <T> T instance(final Constructor<T> constructor)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance();
  }

  public static <T> T instance(final Constructor<T> constructor, final Object... arguments)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance(arguments);
  }

  public static Object getValueFromEnum(final Class<?> enumClass, final String enumName) {
    return Enum.valueOf(enumClass.asSubclass(Enum.class), enumName);
  }

  public static Object getValueFromEnum(final Class<?> enumClass, final String enumName, final int fallbackOrdinal) {
    try {
      return getValueFromEnum(enumClass, enumName);
    } catch (IllegalArgumentException exception) {
      final Object[] constants = enumClass.getEnumConstants();
      if (constants.length > fallbackOrdinal) {
        return constants[fallbackOrdinal];
      }

      throw exception;
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T invoke(final Method method, final Object instance, final Object... arguments)
      throws InvocationTargetException, IllegalAccessException {
    return (T) method.invoke(instance, arguments);
  }

  public static <T> T invokeStatic(final Method method, final Object... arguments)
      throws InvocationTargetException, IllegalAccessException {
    return invoke(method, null, arguments);
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(final Field field, final Object instance) throws IllegalAccessException {
    return (T) field.get(instance);
  }

  public static <T> T getStatic(final Field field) throws IllegalAccessException {
    return get(field, null);
  }

  public static void setField(final Object object, final Class<?> fieldType, final Object value) throws ReflectiveOperationException {
    final Field[] fields = Arrays.stream(object.getClass().getDeclaredFields())
        .filter((field) -> !Modifier.isStatic(field.getModifiers()))
        .toArray(Field[]::new);

    for (Field field : fields) {
      if (field.getType() == fieldType) {
        field.set(object, value);
      }
    }
  }

}

package com.celeste.library.spigot.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionNms {

  private static final String VERSION = Bukkit.getServer().getClass().getPackage().getName()
      .split("\\.")[3];
  private static final String PATH_NMS = "net.minecraft.server." + VERSION + ".";
  private static final String PATH_OBC = "org.bukkit.craftbukkit." + VERSION + ".";

  /**
   * Sends a packet to the CommandSender
   *
   * @param sender CommandSender Player who should receive the packet
   * @param packet Object An object that can be converted to packets
   */
  @SneakyThrows
  public static void sendPacket(final CommandSender sender, final Object packet) {
    final Method getHandle = getMethod(sender.getClass(), "getHandle");
    final Object handle = invoke(getHandle, sender);

    final Field playerConnection = getField(handle.getClass(), "playerConnection");
    final Object connection = get(playerConnection, handle);

    final Method sendPacket = getMethod(connection.getClass(), "sendPacket", getNMS("Packet"));
    invoke(sendPacket, connection, packet);
  }

  /**
   * @param nms Name of the NMS class
   * @return An NMS class
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Class<?> getNMS(@NotNull final String nms) throws ClassNotFoundException {
    return Class.forName(PATH_NMS + nms);
  }

  /**
   * @param obc Name of the OBC class
   * @return An OBC class
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Class<?> getOBC(@NotNull final String obc) throws ClassNotFoundException {
    return Class.forName(PATH_OBC + obc);
  }

  /**
   * @param path Packages and the class name
   * @return Class found from the path
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Class<?> getClazz(@NotNull final String path) throws ClassNotFoundException {
    return Class.forName(path);
  }

  /**
   * @param field A global variable
   * @return Class Field class
   */
  @NotNull
  public static Class<?> getClazz(@NotNull final Field field) {
    return field.getType();
  }

  /**
   * @param path Packages and the class name
   * @return All public sub-classes
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Class<?>[] getClasses(@NotNull final String path) throws ClassNotFoundException {
    return getClazz(path).getClasses();
  }

  /**
   * @param path Packages and the class name
   * @param size Number of the array you want to get
   * @return A public sub-class
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Class<?> getClasses(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getClasses()[size];
  }

  /**
   * @param clazz A class
   * @return All public sub-classes
   */
  @NotNull
  public static Class<?>[] getClasses(@NotNull final Class<?> clazz) {
    return clazz.getClasses();
  }

  /**
   * @param clazz A class
   * @param size  Number of the array you want to get
   * @return A public sub-class
   */
  @NotNull
  public static Class<?> getClasses(@NotNull final Class<?> clazz, final int size) {
    return clazz.getClasses()[size];
  }

  /**
   * @param path Packages and the class name
   * @return All private sub-classes
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Class<?>[] getDcClasses(@NotNull final String path) throws ClassNotFoundException {
    return getClazz(path).getDeclaredClasses();
  }

  /**
   * @param path Packages and the class name
   * @param size Number of the array you want to get
   * @return A private sub-class
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Class<?> getDcClasses(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getDeclaredClasses()[size];
  }

  /**
   * @param clazz A class
   * @return All private sub-classes
   */
  @NotNull
  public static Class<?>[] getDcClasses(@NotNull final Class<?> clazz) {
    return clazz.getDeclaredClasses();
  }

  /**
   * @param clazz A class
   * @param size  Number of the array you want to get
   * @return A private sub-class
   */
  @NotNull
  public static Class<?> getDcClasses(@NotNull final Class<?> clazz, final int size) {
    return clazz.getDeclaredClasses()[size];
  }

  /**
   * @param path           Packages and the class name
   * @param parameterClass Class of the parameters
   * @return A public constructor
   * @throws ClassNotFoundException If class was not found
   * @throws NoSuchMethodException  Throws when method doesn't exists
   */
  @NotNull
  public static Constructor<?> getConstructor(@NotNull final String path,
      @NotNull final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    return getClazz(path).getConstructor(parameterClass);
  }

  /**
   * @param clazz          A class
   * @param parameterClass Class of the parameters
   * @return A public constructor
   * @throws NoSuchMethodException If the method is not found
   */
  @NotNull
  public static Constructor<?> getConstructor(@NotNull final Class<?> clazz,
      @NotNull final Class<?>... parameterClass)
      throws NoSuchMethodException {
    return clazz.getConstructor(parameterClass);
  }

  /**
   * @param path           Packages and the class name
   * @param parameterClass Class of the parameters
   * @return A private constructor
   * @throws ClassNotFoundException If class was not found
   * @throws NoSuchMethodException  If the method is not found
   */
  @NotNull
  public static Constructor<?> getDcConstructor(@NotNull final String path,
      @NotNull final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    final Constructor<?> constructor = getClazz(path).getDeclaredConstructor(parameterClass);
    constructor.setAccessible(true);
    return constructor;
  }

  /**
   * @param clazz          A class
   * @param parameterClass Class of the parameters
   * @return A private constructor
   * @throws NoSuchMethodException If the method is not found
   */
  @NotNull
  public static Constructor<?> getDcConstructor(@NotNull final Class<?> clazz,
      @NotNull final Class<?>... parameterClass)
      throws NoSuchMethodException {
    final Constructor<?> constructor = clazz.getDeclaredConstructor(parameterClass);
    constructor.setAccessible(true);
    return constructor;
  }

  /**
   * @param path Packages and the class name
   * @return All public constructors
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Constructor<?>[] getConstructors(@NotNull final String path)
      throws ClassNotFoundException {
    return getClazz(path).getConstructors();
  }

  /**
   * @param path Packages and the class name
   * @param size Number of the array you want to get
   * @return A public constructor
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Constructor<?> getConstructors(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getConstructors()[size];
  }

  /**
   * @param clazz A class
   * @return All public constructors
   */
  @NotNull
  public static Constructor<?>[] getConstructors(@NotNull final Class<?> clazz) {
    return clazz.getConstructors();
  }

  /**
   * @param clazz A class
   * @param size  Number of the array you want to get
   * @return A public constructor
   */
  @NotNull
  public static Constructor<?> getConstructors(@NotNull final Class<?> clazz, final int size) {
    return clazz.getConstructors()[size];
  }

  /**
   * @param path Packages and the class name
   * @return All private constructors
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Constructor<?>[] getDcConstructors(@NotNull final String path)
      throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredConstructors())
        .peek(constructor -> constructor.setAccessible(true))
        .toArray(Constructor[]::new);
  }

  /**
   * @param path Packages and the class name
   * @param size Number of the array you want to get
   * @return A private constructor
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Constructor<?> getDcConstructors(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    final Constructor<?> constructor = getClazz(path).getDeclaredConstructors()[size];
    constructor.setAccessible(true);
    return constructor;
  }

  /**
   * @param clazz A class
   * @return All private constructors
   */
  @NotNull
  public static Constructor<?>[] getDcConstructors(@NotNull final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredConstructors())
        .peek(constructor -> constructor.setAccessible(true))
        .toArray(Constructor[]::new);
  }

  /**
   * @param clazz A class
   * @param size  Number of the array you want to get
   * @return A private constructor
   */
  @NotNull
  public static Constructor<?> getDcConstructors(@NotNull final Class<?> clazz, final int size) {
    final Constructor<?> constructor = clazz.getDeclaredConstructors()[size];
    constructor.setAccessible(true);
    return constructor;
  }

  /**
   * @param path           Packages and the class name
   * @param methodName     Method name
   * @param parameterClass Class of the parameters
   * @return A public method
   * @throws ClassNotFoundException If class was not found
   * @throws NoSuchMethodException  If the method is not found
   */
  @NotNull
  public static Method getMethod(@NotNull final String path, @NotNull final String methodName,
      @NotNull final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    return getClazz(path).getMethod(methodName, parameterClass);
  }

  /**
   * @param clazz          A class
   * @param methodName     Method name
   * @param parameterClass Class of the parameters
   * @return A public method
   * @throws NoSuchMethodException If the method is not found
   */
  @NotNull
  public static Method getMethod(@NotNull final Class<?> clazz, @NotNull final String methodName,
      @NotNull final Class<?>... parameterClass)
      throws NoSuchMethodException {
    return clazz.getMethod(methodName, parameterClass);
  }

  /**
   * @param path           Packages and the class name
   * @param methodName     Method name
   * @param parameterClass Class of the parameters
   * @return A private method
   * @throws ClassNotFoundException If class was not found
   * @throws NoSuchMethodException  If the method is not found
   */
  @NotNull
  public static Method getDcMethod(@NotNull final String path, @NotNull final String methodName,
      @NotNull final Class<?>... parameterClass)
      throws ClassNotFoundException, NoSuchMethodException {
    final Method method = getClazz(path).getDeclaredMethod(methodName, parameterClass);
    method.setAccessible(true);
    return method;
  }

  /**
   * @param clazz          A class
   * @param methodName     Method name
   * @param parameterClass Class of the parameters
   * @return A private method
   * @throws NoSuchMethodException If the method is not found
   */
  @NotNull
  public static Method getDcMethod(@NotNull final Class<?> clazz, @NotNull final String methodName,
      @NotNull final Class<?>... parameterClass)
      throws NoSuchMethodException {
    final Method method = clazz.getDeclaredMethod(methodName, parameterClass);
    method.setAccessible(true);
    return method;
  }

  /**
   * @param path Packages and the class name
   * @return All public methods
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Method[] getMethods(@NotNull final String path) throws ClassNotFoundException {
    return getClazz(path).getMethods();
  }

  /**
   * @param path Packages and the class name
   * @param size Number of the array you want to get
   * @return A public method
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Method getMethods(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getMethods()[size];
  }

  /**
   * @param clazz A class
   * @return All public methods
   */
  @NotNull
  public static Method[] getMethods(@NotNull final Class<?> clazz) {
    return clazz.getMethods();
  }

  /**
   * @param clazz A class
   * @param size  Number of the array you want to get
   * @return A public method
   */
  @NotNull
  public static Method getMethods(@NotNull final Class<?> clazz, final int size) {
    return clazz.getMethods()[size];
  }

  /**
   * @param path Packages and the class name
   * @return All private method
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Method[] getDcMethods(@NotNull final String path) throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredMethods())
        .peek(method -> method.setAccessible(true))
        .toArray(Method[]::new);
  }

  /**
   * @param path Packages and the class name
   * @param size Number of the array you want to get
   * @return A private method
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Method getDcMethods(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    final Method method = getClazz(path).getDeclaredMethods()[size];
    method.setAccessible(true);
    return method;
  }

  /**
   * @param clazz A class
   * @return All private methods
   */
  @NotNull
  public static Method[] getDcMethods(@NotNull final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredMethods())
        .peek(method -> method.setAccessible(true))
        .toArray(Method[]::new);
  }

  /**
   * @param clazz A class
   * @param size  Number of the array you want to get
   * @return A private method
   */
  @NotNull
  public static Method getDcMethods(@NotNull final Class<?> clazz, final int size) {
    final Method method = clazz.getDeclaredMethods()[size];
    method.setAccessible(true);
    return method;
  }

  /**
   * @param path         Packages and the class name
   * @param variableName Variable name
   * @return A public global variable
   * @throws ClassNotFoundException If class was not found
   * @throws NoSuchFieldException   If doesn't have a field of a specified name
   */
  @NotNull
  public static Field getField(@NotNull final String path, @NotNull final String variableName)
      throws ClassNotFoundException, NoSuchFieldException {
    return getClazz(path).getField(variableName);
  }

  /**
   * @param clazz        A class
   * @param variableName Variable name
   * @return A public global variable
   * @throws NoSuchFieldException If doesn't have a field of a specified name
   */
  @NotNull
  public static Field getField(@NotNull final Class<?> clazz, @NotNull final String variableName)
      throws NoSuchFieldException {
    return clazz.getField(variableName);
  }

  /**
   * @param path         Packages and the class name
   * @param variableName Variable name
   * @return A private global variable
   * @throws ClassNotFoundException If class was not found
   * @throws NoSuchFieldException   If doesn't have a field of a specified name
   */
  @NotNull
  public static Field getDcField(@NotNull final String path, @NotNull final String variableName)
      throws ClassNotFoundException, NoSuchFieldException {
    final Field field = getClazz(path).getDeclaredField(variableName);
    field.setAccessible(true);
    return field;
  }

  /**
   * @param clazz        A class
   * @param variableName Variable name
   * @return A private global variable
   * @throws NoSuchFieldException If doesn't have a field of a specified name
   */
  @NotNull
  public static Field getDcField(@NotNull final Class<?> clazz, @NotNull final String variableName)
      throws NoSuchFieldException {
    final Field field = clazz.getDeclaredField(variableName);
    field.setAccessible(true);
    return field;
  }

  /**
   * @param path Packages and the class name
   * @return All public global variables
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Field[] getFields(@NotNull final String path) throws ClassNotFoundException {
    return getClazz(path).getFields();
  }

  /**
   * @param path Packages and the class name
   * @param size Number of the array you want to get
   * @return A public global variable
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Field getFields(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    return getClazz(path).getFields()[size];
  }

  /**
   * @param clazz A class
   * @return All public global variables
   */
  @NotNull
  public static Field[] getFields(@NotNull final Class<?> clazz) {
    return clazz.getFields();
  }

  /**
   * @param clazz A class
   * @param size  Number of the array you want to get
   * @return A public global variable
   */
  @NotNull
  public static Field getFields(@NotNull final Class<?> clazz, final int size) {
    return clazz.getFields()[size];
  }

  /**
   * @param path Packages and the class name
   * @return All private global variables
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Field[] getDcFields(@NotNull final String path) throws ClassNotFoundException {
    return Arrays.stream(getClazz(path).getDeclaredFields())
        .peek(field -> field.setAccessible(true))
        .toArray(Field[]::new);
  }

  /**
   * @param path Packages and the class name
   * @param size Number of the array you want to get
   * @return A private global variable
   * @throws ClassNotFoundException If class was not found
   */
  @NotNull
  public static Field getDcFields(@NotNull final String path, final int size)
      throws ClassNotFoundException {
    final Field field = getClazz(path).getDeclaredFields()[size];
    field.setAccessible(true);
    return field;
  }

  /**
   * @param clazz A class
   * @return All private global variables
   */
  @NotNull
  public static Field[] getDcFields(@NotNull final Class<?> clazz) {
    return Arrays.stream(clazz.getDeclaredFields())
        .peek(field -> field.setAccessible(true))
        .toArray(Field[]::new);
  }

  /**
   * @param clazz A class
   * @param size  Number of the array you want to get
   * @return A private global variable
   */
  @NotNull
  public static Field getDcFields(@NotNull final Class<?> clazz, final int size) {
    final Field field = clazz.getDeclaredFields()[size];
    field.setAccessible(true);
    return field;
  }

  /**
   * @param constructor A constructor of a class
   * @return An instance of this class
   * @throws IllegalAccessException    If you try to access a private or protected class without
   *                                   enabling
   * @throws InvocationTargetException If when invoking the method, the method does not exist or has
   *                                   incorrect parameters
   * @throws InstantiationException    If you instantiate an abstract class or an interface or there
   *                                   is no constructor in that class
   */
  @NotNull
  public static Object instance(@NotNull final Constructor<?> constructor)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance();
  }

  /**
   * @param constructor A constructor of a class
   * @param args        Constructor parameters
   * @return An instance of this class
   * @throws IllegalAccessException    If you try to access a private or protected class without
   *                                   enabling
   * @throws InvocationTargetException If when invoking the method, the method does not exist or has
   *                                   incorrect parameters
   * @throws InstantiationException    If you instantiate an abstract class or an interface or there
   *                                   is no constructor in that class
   */
  @NotNull
  public static Object instance(@NotNull final Constructor<?> constructor,
      @NotNull final Object... args)
      throws IllegalAccessException, InvocationTargetException, InstantiationException {
    return constructor.newInstance(args);
  }

  /**
   * @param method   Method that will be invoked
   * @param instance An instance of the class of this method
   * @param args     Method parameters
   * @return Result of the method invoked
   * @throws InvocationTargetException If when invoking the method, the method does not exist or has
   *                                   incorrect parameters
   * @throws IllegalAccessException    If you try to access a private or protected class without
   *                                   enabling
   */
  @NotNull
  public static Object invoke(@NotNull final Method method, @NotNull final Object instance,
      @NotNull final Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return method.invoke(instance, args);
  }

  /**
   * @param method Method that will be invoked
   * @param args   Method parameters
   * @return Result of the method invoked
   * @throws InvocationTargetException If when invoking the method, the method does not exist or has
   *                                   incorrect parameters
   * @throws IllegalAccessException    If you try to access a private or protected class without
   *                                   enabling
   */
  @NotNull
  public static Object invokeStatic(@NotNull final Method method, @NotNull final Object... args)
      throws InvocationTargetException, IllegalAccessException {
    return method.invoke(null, args);
  }

  /**
   * @param field    A global variable
   * @param instance An instance of the field class
   * @return An instance of the field class
   * @throws IllegalAccessException If you try to access a private or protected class without
   *                                enabling
   */
  @NotNull
  public static Object get(@NotNull final Field field, @NotNull final Object instance)
      throws IllegalAccessException {
    return field.get(instance);
  }

  /**
   * @param field A global variable
   * @return An instance of the field class
   * @throws IllegalAccessException If you try to access a private or protected class without
   *                                enabling
   */
  @NotNull
  public static Object getStatic(@NotNull final Field field) throws IllegalAccessException {
    return field.get(null);
  }

  /**
   * @param checkVersion Version number that will be checked EX: (16, 11, 8, 7)
   * @return If the version is the same
   */
  public static boolean isEquals(final int checkVersion) {
    return Integer.parseInt(VERSION.split("_")[1]) == checkVersion;
  }

  /**
   * @param checkVersion Version number that will be checked EX: (16, 11, 8, 7)
   * @return If the version is equal or more recent
   */
  public static boolean isEqualsOrMoreRecent(final int checkVersion) {
    return Integer.parseInt(VERSION.split("_")[1]) >= checkVersion;
  }

  /**
   * @param checkVersion Version number that will be checked EX: (16, 11, 8, 7)
   * @return If the version is equal or less recent
   */
  public static boolean isEqualsOrLessRecent(final int checkVersion) {
    return Integer.parseInt(VERSION.split("_")[1]) <= checkVersion;
  }

}

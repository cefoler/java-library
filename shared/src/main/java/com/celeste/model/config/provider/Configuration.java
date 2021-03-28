package com.celeste.model.config.provider;

import com.celeste.model.config.exception.FailedCreateException;
import com.celeste.model.config.exception.FailedLoadException;
import com.celeste.model.config.exception.FailedSaveException;
import com.celeste.model.config.ConfigurationType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

@SuppressWarnings("unused")
public interface Configuration {

  /**
   * Loads the configuration
   * @throws FileNotFoundException Throws when file was not found
   * @throws FailedLoadException Throws when it wasn't able to load the file
   */
  void load() throws FileNotFoundException, FailedLoadException;

  /**
   * Saves the configuration
   * @throws FailedSaveException Throws when file wasn't able to save
   */
  void save() throws FailedSaveException;

  /**
   * Saves and update the configuration
   * @throws FailedLoadException Throws when it fails to load
   * @throws FailedSaveException Throws when it fails to save
   */
  void saveAndUpdate() throws FailedSaveException, FailedLoadException;

  /**
   * Check if the value contains in the configuration
   * @param path String
   * @return boolean
   */
  boolean contains(@NotNull String path);

  /**
   * Sets a value into the configuration
   * @param path String
   * @param object Object
   */
  void set(@NotNull String path, @Nullable Object object);

  /**
   * Gets the value from path in the configuration
   *
   * @param path String
   * @param <T> Object
   *
   * @return Object
   */
  @NotNull
  <T> T get(@NotNull String path);

  /**
   * Returns a generic value from that path
   *
   * @param path String
   * @param orElseFound T
   * @param <T> T
   * @return T
   */
  @NotNull
  <T> T get(@NotNull String path, @NotNull T orElseFound);

  /**
   * Gets Object from path
   * @param path String
   *
   * @return Object
   */
  @NotNull
  Object getObject(@NotNull String path);

  /**
   * Gets Object from path, if nulls return a value
   * @param path String
   * @param orElseFound String
   *
   * @return Object
   */
  @NotNull
  Object getObject(@NotNull String path, @NotNull String orElseFound);

  /**
   * Gets string from path
   * @param path String
   *
   * @return string
   */
  @NotNull
  String getString(@NotNull String path);

  /**
   * Gets string from path, if nulls return a value
   * @param path String
   * @param orElseFound string
   *
   * @return string
   */
  @NotNull
  String getString(@NotNull String path, @NotNull String orElseFound);

  /**
   * Gets int from path
   * @param path String
   *
   * @return int
   */
  int getInt(@NotNull String path);

  /**
   * Gets int from path, if nulls return a value
   * @param path String
   * @param orElseFound int
   *
   * @return int
   */
  int getInt(@NotNull String path, int orElseFound);

  /**
   * Gets long from path
   * @param path String
   *
   * @return long
   */
  long getLong(@NotNull String path);

  /**
   * Gets long from path, if nulls return a value
   * @param path String
   * @param orElseFound long
   *
   * @return long
   */
  long getLong(@NotNull String path, long orElseFound);

  /**
   * Gets double from path
   * @param path String
   *
   * @return double
   */
  double getDouble(@NotNull String path);

  /**
   * Gets double from path, if nulls return a value
   * @param path String
   * @param orElseFound double
   *
   * @return double
   */
  double getDouble(@NotNull String path, double orElseFound);

  /**
   * Gets boolean from path
   * @param path String
   *
   * @return boolean
   */
  boolean getBoolean(@NotNull String path);

  /**
   * Gets boolean from path, if nulls return a value
   * @param path String
   * @param orElseFound boolean
   *
   * @return boolean
   */
  boolean getBoolean(@NotNull String path, boolean orElseFound);

  /**
   * Get list from path
   * @param path String
   * @return List
   */
  @NotNull
  List<?> getList(@NotNull String path);

  /**
   * Get list from path, if nulls return a value
   * @param path String
   * @param orElseFound List
   *
   * @return List
   */
  @NotNull
  List<?> getList(@NotNull String path, @NotNull List<?> orElseFound);

  /**
   * Get string list from path
   * @param path String
   * @return List
   */
  @NotNull
  List<String> getStringList(@NotNull String path);

  /**
   * Get integer list from path
   * @param path String
   * @return List
   */
  @NotNull
  List<Integer> getIntegerList(@NotNull String path);

  /**
   * Get long list from path
   * @param path String
   * @return List
   */
  @NotNull
  List<Long> getLongList(@NotNull String path);

  /**
   * Get double list from path
   * @param path String
   * @return List
   */
  @NotNull
  List<Double> getDoubleList(@NotNull String path);

  /**
   * Get boolean list from path
   * @param path String
   * @return List
   */
  @NotNull
  List<Boolean> getBooleanList(@NotNull String path);

  /**
   *
   * @param path String
   * @return Set
   */
  @NotNull
  Set<String> getKeys(@NotNull final String path);

  /**
   * Returns the configuration file
   * @return File
   */
  @NotNull
  File getFile();

  /**
   * Get the type of the configuration
   * @return ConfigurationType
   */
  @NotNull
  ConfigurationType getType();

  /**
   * Gets the InputStream from the path
   * @param resourcePath String
   *
   * @return InputStream
   */
  @NotNull
  default InputStream getResource(@NotNull final String resourcePath) {
    final InputStream input = getClass().getClassLoader().getResourceAsStream(resourcePath);
    if (input == null) {
      throw new IllegalArgumentException(resourcePath + " not found");
    }

    return input;
  }

  /**
   * Creates the configuration file
   * @param path String
   * @param resourcePath String
   *
   * @return File
   */
  default File create(final String path, final String resourcePath) {
    final String directories = resourcePath.contains("/") ? resourcePath.substring(0, resourcePath.lastIndexOf("/")) : "";
    final File file = new File(path, directories);

    if (!file.exists()) {
      file.mkdirs();
    }

    return new File(path, resourcePath);
  }

  /**
   * Copies a configuration
   * @param input InputStream
   * @param output File
   *
   * @throws FailedCreateException Throws when it fails to create the new configuration
   */
  default void copy(@NotNull final InputStream input, @NotNull final File output) throws FailedCreateException {
    try (
        Scanner scanner = new Scanner(input);
        PrintStream print = new PrintStream(output)
    ) {
      while (scanner.hasNext()) {
        print.println(scanner.nextLine());
      }
    } catch (Throwable exception) {
      throw new FailedCreateException(exception);
    }
  }

}

package com.celeste.model.config.provider;

import com.celeste.model.config.exception.FailedCreateException;
import com.celeste.model.config.exception.FailedGetException;
import com.celeste.model.config.exception.FailedLoadException;
import com.celeste.model.config.exception.FailedSaveException;
import com.celeste.model.config.ConfigurationType;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public final class YamlProvider implements Configuration {

  private final Constructor constructor;
  private final Representer representer;
  private final DumperOptions options;

  private final Yaml yaml;

  private final File file;

  private LinkedHashMap<?, ?> config;

  /**
   * Creates a new YamlProvider
   *
   * @param path String
   * @param resourcePath String
   * @param replace boolean
   *
   * @throws FailedCreateException Throws when it wasn't possible to create the configuration
   * @throws FailedLoadException Throws when it wasn't possible to load the configuration
   */
  public YamlProvider(@NotNull final String path, @NotNull final String resourcePath, final boolean replace
  ) throws FailedCreateException, FailedLoadException {
    this.constructor = new Constructor();
    this.representer = new Representer();
    this.options = new DumperOptions();

    options.setIndent(2);
    options.setAllowUnicode(true);
    options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

    this.yaml = new Yaml(constructor, representer, options);
    this.file = create(path, resourcePath);

    if (!file.exists() || replace) {
      copy(getResource(resourcePath), file);
    }

    load();
  }

  /**
   * Loads the configuration
   * @throws FailedLoadException Throws when it fails to load the configuration
   */
  @Override
  public void load() throws FailedLoadException {
    try (
        FileInputStream input = new FileInputStream(file);
        Reader reader = new InputStreamReader(input, Charset.defaultCharset())
    ) {
      this.config = yaml.loadAs(reader, LinkedHashMap.class);
    } catch (Throwable throwable) {
      throw new FailedLoadException(throwable);
    }
  }

  /**
   * Saves the configuration
   * @throws FailedSaveException Throws when it wasn't possible to save the configuration
   */
  @Override
  public void save() throws FailedSaveException {
    try (
        FileOutputStream output = new FileOutputStream(file);
        Writer writer = new OutputStreamWriter(output, Charset.defaultCharset())
    ) {
      yaml.dump(config, writer);
    } catch (Throwable throwable) {
      throw new FailedSaveException(throwable);
    }
  }

  /**
   * Saves and update the configuration
   * @throws FailedLoadException Throws when it fails to load
   * @throws FailedSaveException Throws when it fails to save
   */
  @Override
  public void saveAndUpdate() throws FailedLoadException, FailedSaveException {
    save();
    load();
  }

  /**
   * Check if the value contains in the configuration
   * @param path String
   * @return boolean
   */
  @Override
  public boolean contains(final @NotNull String path) {
    return getResult(path) != null;
  }

  /**
   * Sets a value into the configuration
   * @param path String
   * @param object Object
   */
  @Override @SuppressWarnings("unchecked")
  public void set(final @NotNull String path, final @Nullable Object object) {
    final String[] split = path.split("\\.");
    final String lastPath = split[split.length - 1];

    Object result = config.clone();

    for (final String key : split) {
      if (!(result instanceof Map<?, ?>)) {
        throw new FailedGetException("The path " + path + " was not found");
      }

      final Map<String, Object> newConfig = (Map<String, Object>) result;
      if (newConfig.containsKey(lastPath)) {
        if (object == null) {
          newConfig.remove(lastPath);
        } else {
          newConfig.put(lastPath, object);
        }

        return;
      }

      result = newConfig.get(key);
    }

    this.config = (LinkedHashMap<?, ?>) result;
  }

  /**
   * Gets the value from path in the configuration
   *
   * @param path String
   * @param <T> Object
   *
   * @return Object
   */
  @Override @NotNull @SuppressWarnings("unchecked")
  public <T> T get(final @NotNull String path) {
    final Object result = getResult(path);
    if (result == null) {
      throw new FailedGetException("The path " + path + " was not found");
    }

    return (T) result;
  }

  /**
   * Returns a generic value from that path
   *
   * @param path String
   * @param orElseFound T
   * @param <T> T
   * @return T
   */
  @Override @NotNull @SuppressWarnings("unchecked")
  public <T> T get(final @NotNull String path, @NotNull final T orElseFound) {
    final Object result = getResult(path);
    return result == null ? orElseFound : (T) result;
  }

  /**
   * Gets Object from path
   * @param path String
   *
   * @return Object
   */
  @Override @NotNull
  public Object getObject(final @NotNull String path) {
    return get(path);
  }

  /**
   * Gets Object from path, if nulls return a value
   * @param path String
   * @param orElseFound String
   *
   * @return Object
   */
  @Override @NotNull
  public Object getObject(final @NotNull String path, final @NotNull String orElseFound) {
    return get(path, orElseFound);
  }

  /**
   * Gets string from path
   * @param path String
   *
   * @return string
   */
  @Override @NotNull
  public String getString(final @NotNull String path) {
    return get(path);
  }

  /**
   * Gets string from path, if nulls return a value
   * @param path String
   * @param orElseFound string
   *
   * @return string
   */
  @Override @NotNull
  public String getString(final @NotNull String path, final @NotNull String orElseFound) {
    return get(path, orElseFound);
  }

  /**
   * Gets int from path
   * @param path String
   *
   * @return int
   */
  @Override
  public int getInt(final @NotNull String path) {
    return get(path);
  }

  /**
   * Gets int from path, if nulls return a value
   * @param path String
   * @param orElseFound int
   *
   * @return int
   */
  @Override
  public int getInt(final @NotNull String path, final int orElseFound) {
    return get(path, orElseFound);
  }

  /**
   * Gets long from path
   * @param path String
   *
   * @return long
   */
  @Override
  public long getLong(final @NotNull String path) {
    return get(path);
  }

  /**
   * Gets long from path, if nulls return a value
   * @param path String
   * @param orElseFound long
   *
   * @return long
   */
  @Override
  public long getLong(final @NotNull String path, final long orElseFound) {
    return get(path, orElseFound);
  }

  /**
   * Gets double from path
   * @param path String
   *
   * @return double
   */
  @Override
  public double getDouble(final @NotNull String path) {
    return get(path);
  }

  /**
   * Gets double from path, if nulls return a value
   * @param path String
   * @param orElseFound double
   *
   * @return double
   */
  @Override
  public double getDouble(final @NotNull String path, final double orElseFound) {
    return get(path, orElseFound);
  }

  /**
   * Gets boolean from path
   * @param path String
   *
   * @return boolean
   */
  @Override
  public boolean getBoolean(final @NotNull String path) {
    return get(path);
  }

  /**
   * Gets boolean from path, if nulls return a value
   * @param path String
   * @param orElseFound boolean
   *
   * @return boolean
   */
  @Override
  public boolean getBoolean(final @NotNull String path, final boolean orElseFound) {
    return get(path, orElseFound);
  }

  /**
   * Get list from path
   * @param path String
   * @return List
   */
  @Override @NotNull
  public List<?> getList(final @NotNull String path) {
    return get(path);
  }

  /**
   * Get list from path
   * @param path String
   * @return List
   */
  @Override @NotNull
  public List<?> getList(final @NotNull String path, final @NotNull List<?> orElseFound) {
    return get(path, orElseFound);
  }

  /**
   * Get string list from path
   * @param path String
   * @return List
   */
  @Override @NotNull
  public List<String> getStringList(final @NotNull String path) {
    return get(path, new ArrayList<>());
  }

  /**
   * Get integer list from path
   * @param path String
   * @return List
   */
  @Override @NotNull
  public List<Integer> getIntegerList(final @NotNull String path) {
    return get(path, new ArrayList<>());
  }

  /**
   * Get long list from path
   * @param path String
   * @return List
   */
  @Override @NotNull
  public List<Long> getLongList(final @NotNull String path) {
    return get(path, new ArrayList<>());
  }

  /**
   * Get double list from path
   * @param path String
   * @return List
   */
  @Override @NotNull
  public List<Double> getDoubleList(final @NotNull String path) {
    return get(path, new ArrayList<>());
  }

  /**
   * Get boolean list from path
   * @param path String
   * @return List
   */
  @Override @NotNull
  public List<Boolean> getBooleanList(final @NotNull String path) {
    return get(path, new ArrayList<>());
  }

  /**
   *
   * @param path String
   * @return Set
   */
  @Override @NotNull
  public Set<String> getKeys(@NotNull final String path) {
    Object result = config.clone();

    for (final String key : path.split("\\.")) {
      if (!(result instanceof Map<?, ?>)) {
        throw new FailedGetException("The path " + path + " was not found");
      }

      result = ((Map<?, ?>) result).get(key);
    }

    if (!(result instanceof Map<?, ?>)) {
      throw new FailedGetException("The path " + path + " was not found");
    }

    return ((Map<?, ?>) result).keySet().stream()
        .map(Object::toString)
        .collect(Collectors.toSet());
  }

  /**
   * Get the type of the configuration
   * @return ConfigurationType
   */
  @Override @NotNull
  public ConfigurationType getType() {
    return ConfigurationType.YAML;
  }

  /**
   * Gets the result from the path
   * @param path String
   * @return Object
   */
  @Nullable @SuppressWarnings("unchecked")
  private Object getResult(final @NotNull String path) {
    Object result = config.clone();

    for (final String key : path.split("\\.")) {
      if (!(result instanceof Map<?, ?>)) {
        result = null;
        break;
      }

      result = ((Map<String, String>) result).get(key);
    }

    return result;
  }

}

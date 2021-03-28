package com.celeste.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class GsonAdapter {

  private static final GsonAdapter INSTANCE = new GsonAdapter();

  private Gson gson;

  /**
   * Creates the GsonAdapter
   */
  private GsonAdapter() {
    this.gson = new GsonBuilder()
        .enableComplexMapKeySerialization()
        .create();
  }

  /**
   * Registers a Adapter
   *
   * @param type Class
   * @param subTypes Class...
   * @param <T> T
   */
  @SafeVarargs
  public final <T> void registryAdapter(@NotNull final Class<T> type, @NotNull final Class<? extends T>... subTypes) {
    final GsonTypeAdapter<T> adapter = GsonTypeAdapter.of(type)
        .registerSubtypes(subTypes);

    this.gson = gson.newBuilder()
        .registerTypeAdapterFactory(adapter)
        .create();
  }

  /**
   * Transforms the Object into JSON value
   *
   * @param value Object
   * @return String
   */
  @NotNull
  public String toJson(@NotNull final Object value) {
    return gson.toJson(value);
  }

  /**
   * Transforms the json string into a object from that TypeToken
   * @param json String
   * @param typeToken TypeToken
   * @param <T> T
   *
   * @return T
   */
  @Nullable
  public <T> T fromJson(@NotNull final String json, @NotNull final TypeToken<T> typeToken) {
    return gson.fromJson(json, typeToken.getType());
  }

  /**
   * Transforms the json string into a object from that Class
   * @param json String
   * @param clazz Class
   * @param <T> T
   *
   * @return T
   */
  @Nullable
  public <T> T fromJson(@NotNull final String json, @NotNull final Class<T> clazz) {
    return fromJson(json, TypeToken.get(clazz));
  }

  /**
   * Transforms into the object by that ResultSet
   *
   * @param result ResultSet
   * @param column String
   * @param clazz Class
   * @param <T> T
   *
   * @return T
   * @throws SQLException Throws when the value was not found
   */
  @Nullable
  public <T> T fromJson(@NotNull final ResultSet result, @NotNull final String column,
                        @NotNull final Class<T> clazz
  ) throws SQLException {
    return fromJson(result.getString(column), clazz);
  }

  /**
   * @return {@code GsonAdapter}
   */
  @NotNull
  public static GsonAdapter getInstance() {
    return INSTANCE;
  }

}


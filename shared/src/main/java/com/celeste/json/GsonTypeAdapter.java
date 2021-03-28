package com.celeste.json;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class GsonTypeAdapter<T> implements TypeAdapterFactory {

  private final Class<?> baseType;

  private final String typeFieldName;

  private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap<>();
  private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap<>();

  private final boolean maintainType;

  /**
   * Creates a GsonTypeAdapter
   * @param baseType Type
   * @param typeFieldName String
   * @param maintainType boolean
   */
  private GsonTypeAdapter(@NotNull final Class<?> baseType,
                          @NotNull final String typeFieldName, final boolean maintainType
  ) {
    this.baseType = baseType;
    this.typeFieldName = typeFieldName;
    this.maintainType = maintainType;
  }

  /**
   * Creates a adapter from that class
   *
   * @param baseType T
   * @param typeFieldName String
   * @param maintainType boolean
   * @param <T> T
   *
   * @return GsonTypeAdapter
   */
  @NotNull
  public static <T> GsonTypeAdapter<T> of(@NotNull final Class<T> baseType,
                                          @NotNull final String typeFieldName, final boolean maintainType
  ) {
    return new GsonTypeAdapter<>(baseType, typeFieldName, maintainType);
  }

  /**
   * Creates a adapter from that class
   *
   * @param baseType T
   * @param typeFieldName String
   * @param <T> T
   *
   * @return GsonTypeAdapter
   */
  @NotNull
  public static <T> GsonTypeAdapter<T> of(@NotNull final Class<T> baseType, @NotNull final String typeFieldName) {
    return new GsonTypeAdapter<>(baseType, typeFieldName, false);
  }

  /**
   * Creates a adapter from that class
   *
   * @param baseType T
   * @param <T> T
   *
   * @return GsonTypeAdapter
   */
  @NotNull
  public static <T> GsonTypeAdapter<T> of(@NotNull final Class<T> baseType) {
    return new GsonTypeAdapter<>(baseType, "type", false);
  }

  /**
   * Registers a subtype of the adapter
   * @param subType Class
   * @param label String
   *
   * @return GsonTypeAdapter
   */
  @NotNull
  public GsonTypeAdapter<T> registerSubtype(@NotNull final Class<? extends T> subType, @NotNull final String label) {
    if (subtypeToLabel.containsKey(subType) || labelToSubtype.containsKey(label)) {
      throw new IllegalArgumentException("Types and labels must be unique.");
    }

    labelToSubtype.put(label, subType);
    subtypeToLabel.put(subType, label);

    return this;
  }

  /**
   * Registers a subtype
   *
   * @param subType Class
   * @return GsonTypeAdapter
   */
  @NotNull
  public GsonTypeAdapter<T> registerSubtype(@NotNull final Class<? extends T> subType) {
    return registerSubtype(subType, subType.getSimpleName());
  }

  /**
   * Registers subtypes
   *
   * @param subTypes Class
   * @return GsonTypeAdapter
   */
  @NotNull @SafeVarargs
  public final GsonTypeAdapter<T> registerSubtypes(@NotNull final Class<? extends T>... subTypes) {
    Arrays.stream(subTypes).forEach(this::registerSubtype);
    return this;
  }

  /**
   * Creates a TypeAdapter
   *
   * @param gson Gson
   * @param type TypeToken
   * @param <U> Type
   *
   * @return TypeAdapter
   */
  @Nullable
  public <U> TypeAdapter<U> create(@NotNull final Gson gson, @NotNull final TypeToken<U> type) {
    if (type.getRawType() != baseType) {
      return null;
    }

    final Map<String, TypeAdapter<?>> labelToDelegate = new LinkedHashMap<>();
    final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedHashMap<>();

    for (final Entry<String, Class<?>> entry : labelToSubtype.entrySet()) {
      final TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));

      labelToDelegate.put(entry.getKey(), delegate);
      subtypeToDelegate.put(entry.getValue(), delegate);
    }

    return new TypeAdapter<U>() {

      @Override @NotNull @SuppressWarnings("unchecked")
      public U read(@NotNull final JsonReader in) {
        final JsonElement jsonElement = Streams.parse(in);
        final JsonElement labelJsonElement;

        if (maintainType) {
          labelJsonElement = jsonElement.getAsJsonObject().get(typeFieldName);
        } else {
          labelJsonElement = jsonElement.getAsJsonObject().remove(typeFieldName);
        }

        if (labelJsonElement == null) {
          throw new JsonParseException("Cannot deserialize " + baseType + ". It doesn't define a Field named " + typeFieldName);
        }

        final String label = labelJsonElement.getAsString();
        final TypeAdapter<U> delegate = (TypeAdapter<U>) labelToDelegate.get(label);

        if (delegate == null) {
          throw new JsonParseException("Cannot deserialize " + baseType + " on SubType named " + label);
        }

        return delegate.fromJsonTree(jsonElement);
      }

      @Override @SuppressWarnings("unchecked")
      public void write(@NotNull final JsonWriter out, @NotNull final U value) throws IOException {
        final Class<?> srcType = value.getClass();

        final String label = subtypeToLabel.get(srcType);
        final TypeAdapter<U> delegate = (TypeAdapter<U>) subtypeToDelegate.get(srcType);

        if (delegate == null) {
          throw new JsonParseException("Cannot serialize " + srcType.getName());
        }

        final JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();

        if (maintainType) {
          Streams.write(jsonObject, out);
          return;
        }

        if (jsonObject.has(typeFieldName)) {
          throw new JsonParseException("Cannot serialize " + srcType.getName() + ". It already defines a Field named " + typeFieldName);
        }

        final JsonObject clone = new JsonObject();
        clone.add(typeFieldName, new JsonPrimitive(label));

        for (final Entry<String, JsonElement> entry : jsonObject.entrySet()) {
          clone.add(entry.getKey(), entry.getValue());
        }

        Streams.write(clone, out);
      }
    }.nullSafe();
  }

}

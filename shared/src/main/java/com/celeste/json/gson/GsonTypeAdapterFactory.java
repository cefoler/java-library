package com.celeste.json.gson;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class GsonTypeAdapterFactory<T> implements TypeAdapterFactory {

  private final Class<?> baseType;

  private final String typeFieldName;

  private final Map<String, Class<?>> labelToSubtype = new LinkedHashMap<>();
  private final Map<Class<?>, String> subtypeToLabel = new LinkedHashMap<>();

  private final boolean maintainType;

  private GsonTypeAdapterFactory(final Class<?> baseType, final String typeFieldName, final boolean maintainType) {
    if (typeFieldName == null || baseType == null) throw new NullPointerException();

    this.baseType = baseType;
    this.typeFieldName = typeFieldName;
    this.maintainType = maintainType;
  }

  public static <T> GsonTypeAdapterFactory<T> of(final Class<T> baseType, final String typeFieldName, final boolean maintainType) {
    return new GsonTypeAdapterFactory<>(baseType, typeFieldName, maintainType);
  }

  public static <T> GsonTypeAdapterFactory<T> of(final Class<T> baseType, final String typeFieldName) {
    return new GsonTypeAdapterFactory<>(baseType, typeFieldName, false);
  }

  public static <T> GsonTypeAdapterFactory<T> of(final Class<T> baseType) {
    return new GsonTypeAdapterFactory<>(baseType, "type", false);
  }

  public GsonTypeAdapterFactory<T> registerSubtype(final Class<? extends T> subType, final String label) {
    if (subType == null || label == null) throw new NullPointerException();

    if (subtypeToLabel.containsKey(subType) || labelToSubtype.containsKey(label)) {
      throw new IllegalArgumentException("types and labels must be unique");
    }

    labelToSubtype.put(label, subType);
    subtypeToLabel.put(subType, label);

    return this;
  }

  public GsonTypeAdapterFactory<T> registerSubtype(final Class<? extends T> subType) {
    return registerSubtype(subType, subType.getSimpleName());
  }

  @SafeVarargs
  public final GsonTypeAdapterFactory<T> registerSubtypes(final Class<? extends T>... subTypes) {
    Arrays.stream(subTypes).forEach(this::registerSubtype);
    return this;
  }

  public <U> TypeAdapter<U> create(final Gson gson, final TypeToken<U> type) {
      if (type.getRawType() != baseType) return null;

      final Map<String, TypeAdapter<?>> labelToDelegate = new LinkedHashMap<>();
      final Map<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedHashMap<>();

      for (final Map.Entry<String, Class<?>> entry : labelToSubtype.entrySet()) {
        final TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));

        labelToDelegate.put(entry.getKey(), delegate);
        subtypeToDelegate.put(entry.getValue(), delegate);
  }

    return new TypeAdapter<U>() {

      @Override @SuppressWarnings("unchecked")
      public U read(final JsonReader in) {
        final JsonElement jsonElement = Streams.parse(in);
        final JsonElement labelJsonElement;

        if (maintainType) labelJsonElement = jsonElement.getAsJsonObject().get(typeFieldName);
        else labelJsonElement = jsonElement.getAsJsonObject().remove(typeFieldName);

        if (labelJsonElement == null) {
          throw new JsonParseException("cannot deserialize " + baseType
              + " because it does not define a field named " + typeFieldName
          );
        }

        final String label = labelJsonElement.getAsString();
        final TypeAdapter<U> delegate = (TypeAdapter<U>) labelToDelegate.get(label);

        if (delegate == null) {
          throw new JsonParseException("cannot deserialize " + baseType
              + " subtype named " + label + "; did you forget to register a subtype?");
        }

        return delegate.fromJsonTree(jsonElement);
      }

      @Override @SuppressWarnings("unchecked")
      public void write(final JsonWriter out, final U value) throws IOException {
        final Class<?> srcType = value.getClass();

        final String label = subtypeToLabel.get(srcType);
        final TypeAdapter<U> delegate = (TypeAdapter<U>) subtypeToDelegate.get(srcType);

        if (delegate == null) {
          throw new JsonParseException("cannot serialize " + srcType.getName()
              + "; did you forget to register a subtype?");
        }

        final JsonObject jsonObject = delegate.toJsonTree(value).getAsJsonObject();

        if (maintainType) {
          Streams.write(jsonObject, out);
          return;
        }

        if (jsonObject.has(typeFieldName)) {
          throw new JsonParseException("cannot serialize " + srcType.getName()
              + " because it already defines a field named " + typeFieldName);
        }

        final JsonObject clone = new JsonObject();
        clone.add(typeFieldName, new JsonPrimitive(label));

        for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
          clone.add(entry.getKey(), entry.getValue());
        }

        Streams.write(clone, out);
      }
    }.nullSafe();
  }

}
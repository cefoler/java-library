package com.celeste.json;

import com.celeste.annotation.Utility;
import com.celeste.registries.Registry;
import com.celeste.registries.impl.LinkedRegistry;
import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * Used to create a TypeAdapter for the class.
 *
 * @param <T> Class
 */
@AllArgsConstructor
@Utility
public class GsonTypeAdapterFactory<T> implements TypeAdapterFactory {

    private final Class<?> baseType;
    private final String typeFieldName;

    private final Registry<String, Class<?>> labelToSubtype = new LinkedRegistry<>();
    private final Registry<Class<?>, String> subtypeToLabel = new LinkedRegistry<>();

    private final boolean maintainType;

    /**
     * Creates a TypeAdapter for the class specified.
     *
     * @param baseType Class
     * @param typeFieldName Name
     * @param maintainType Boolean
     * @param <T> Class for the TypeAdapter
     *
     * @return TypeAdapter
     */
    public static <T> GsonTypeAdapterFactory<T> of(final Class<T> baseType, final String typeFieldName, final boolean maintainType) {
        return new GsonTypeAdapterFactory<>(baseType, typeFieldName, maintainType);
    }

    /**
     * Creates a TypeAdapter for the class specified.
     * MaintainType is always false in this method.
     *
     * @param baseType Class
     * @param typeFieldName Name
     * @param <T> Class for the TypeAdapter
     *
     * @return TypeAdapter
     */
    public static <T> GsonTypeAdapterFactory<T> of(final Class<T> baseType, final String typeFieldName) {
        return new GsonTypeAdapterFactory<>(baseType, typeFieldName, false);
    }

    /**
     * Creates a TypeAdapter for the class specified.
     * MaintainType is always false in this method.
     * TypeField is always "type" in this method.
     *
     * @param baseType Class
     * @param <T> Class for the TypeAdapter
     *
     * @return TypeAdapter
     */
    public static <T> GsonTypeAdapterFactory<T> of(final Class<T> baseType) {
        return new GsonTypeAdapterFactory<>(baseType, "type", false);
    }

    /**
     * Registers SubType on the factory.
     *
     * @param subType Class of the subType.
     * @param label Name
     *
     * @return TypeAdapter
     */
    public GsonTypeAdapterFactory<T> registerSubtype(@NotNull final Class<? extends T> subType, @NotNull final String label) {
        if (subtypeToLabel.contains(subType) || labelToSubtype.contains(label)) {
            throw new IllegalArgumentException("Types and labels must be unique.");
        }

        labelToSubtype.register(label, subType);
        subtypeToLabel.register(subType, label);

        return this;
    }

    /**
     * Registers SubType on the factory.
     * The label is always the subType class name.
     *
     * @param subType Class of the subType.
     *
     * @return TypeAdapter
     */
    public GsonTypeAdapterFactory<T> registerSubtype(final Class<? extends T> subType) {
        return registerSubtype(subType, subType.getSimpleName());
    }

    /**
     * Registers multiple subTypes into the factory.
     *
     * @param subTypes Class
     *
     * @return TypeAdapter
     */
    @SafeVarargs
    public final GsonTypeAdapterFactory<T> registerSubtypes(final Class<? extends T>... subTypes) {
        Arrays.stream(subTypes).forEach(this::registerSubtype);
        return this;
    }

    /**
     * Creates a TypeAdapter
     *
     * @param gson Gson instance
     * @param type TypeToken
     * @param <U> Class for the adapter
     *
     * @return TypeAdapter
     */
    public <U> TypeAdapter<U> create(final Gson gson, final TypeToken<U> type) {
        if (type.getRawType() != baseType) {
            return null;
        }

        final LinkedRegistry<String, TypeAdapter<?>> labelToDelegate = new LinkedRegistry<>();
        final LinkedRegistry<Class<?>, TypeAdapter<?>> subtypeToDelegate = new LinkedRegistry<>();

        for (final Map.Entry<String, Class<?>> entry : labelToSubtype.getMap().entrySet()) {
            final TypeAdapter<?> delegate = gson.getDelegateAdapter(this, TypeToken.get(entry.getValue()));

            labelToDelegate.register(entry.getKey(), delegate);
            subtypeToDelegate.register(entry.getValue(), delegate);
        }

        return new TypeAdapter<U>() {

        @Override @SuppressWarnings("unchecked")
        public U read(final JsonReader in) {
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

          return delegate.fromJsonTree(jsonElement);
        }

        @Override @SuppressWarnings("unchecked")
        public void write(final JsonWriter out, final U value) throws IOException {
          final Class<?> srcType = value.getClass();

          final String label = subtypeToLabel.get(srcType);
          final TypeAdapter<U> delegate = (TypeAdapter<U>) subtypeToDelegate.get(srcType);
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

          for (final Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
              clone.add(entry.getKey(), entry.getValue());
          }

          Streams.write(clone, out);
        }

        }.nullSafe();
    }

}

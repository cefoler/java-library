package com.celeste.library.core.adapter.impl.jackson;

import com.celeste.library.core.adapter.Json;
import com.celeste.library.core.adapter.exception.JsonDeserializeException;
import com.celeste.library.core.adapter.exception.JsonSerializeException;
import com.celeste.library.core.util.Validation;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;

public final class JacksonAdapter implements Json {

  private static final JacksonAdapter INSTANCE;

  static {
    INSTANCE = new JacksonAdapter();
  }

  private final ObjectMapper mapper;

  private JacksonAdapter() {
    this.mapper = new ObjectMapper();
  }

  @Override
  public String serialize(final Object value) throws JsonSerializeException {
    try {
      return mapper.writeValueAsString(value);
    } catch (Exception exception) {
      throw new JsonSerializeException(exception);
    }
  }

  @Override
  public <T> T deserialize(final String json, final Class<T> clazz)
      throws JsonDeserializeException {
    try {
      return mapper.readValue(json, clazz);
    } catch (Exception exception) {
      throw new JsonDeserializeException(exception);
    }
  }

  public ObjectNode createNode() {
    return mapper.createObjectNode();
  }

  @SneakyThrows
  public ObjectNode getNode(final String json) {
    return mapper.readValue(json, ObjectNode.class);
  }

  public JsonNode get(final ObjectNode node, final String key) {
    return Validation.notNull(node.get(key), () -> new NullPointerException("The key specified cannot be null"));
  }

  public String getString(final ObjectNode node, final String key) {
    return get(node, key).textValue();
  }

  public int getInt(final ObjectNode node, final String key) {
    return get(node, key).intValue();
  }

  public boolean getBoolean(final ObjectNode node, final String key) {
    return get(node, key).booleanValue();
  }

  public double getDouble(final ObjectNode node, final String key) {
    return get(node, key).doubleValue();
  }

  public float getFloat(final ObjectNode node, final String key) {
    return get(node, key).floatValue();
  }

  public static JacksonAdapter getInstance() {
    return INSTANCE;
  }

}

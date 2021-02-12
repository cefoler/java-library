package com.celeste.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class GsonAdapter {

    private final Gson gson = new GsonBuilder()
      .enableComplexMapKeySerialization()
      .create();

    public final String toJson(Object src) {
        return gson.toJson(src);
    }

    public final <T> T fromJson(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }

    public final <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, TypeToken.get(clazz));
    }

    public final <T> T fromJson(ResultSet resultSet, String column, Class<T> clazz) throws SQLException {
        return fromJson(resultSet.getString(column), clazz);
    }

    public final <T, C extends Collection<T>> C collect(String json) {
        return fromJson(json, new TypeAware<>());
    }

}


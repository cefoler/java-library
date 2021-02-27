package com.celeste.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author Luiza Prestes, Deser
 */
public class GsonAdapter {

    private final Gson gson = new GsonBuilder()
        .enableComplexMapKeySerialization()
        .create();

    /**
     * @param context Object that will be turning to json.
     *
     * @return String Json string.
     */
    public final String toJson(Object context) {
        return gson.toJson(context);
    }

    /**
     * @param json Json formatted string that will be turn into a object.
     * @param typeToken TypeToken with object that that String will be turn into.
     *
     * @return String Json string.
     */
    public final <T> T fromJson(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }

    /**
     * @param json Json formatted string that will be turn into a object.
     * @param clazz Class of object that that String will be turn into.
     *
     * @return String Json string.
     */
    public final <T> T fromJson(String json, Class<T> clazz) {
        return fromJson(json, TypeToken.get(clazz));
    }

}


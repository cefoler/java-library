package com.celeste.util;

import com.celeste.annotation.Utility;

import java.util.Properties;

@Utility
public class PropertiesBuilder {

    private final Properties properties;

    public PropertiesBuilder() {
        this.properties = new Properties();
    }

    /**
     * @param key Key of the value.
     * @param value Value that will be put on that key.
     */
    public PropertiesBuilder with(String key, String value) {
        properties.put(key, value);
        return this;
    }

    /**
     * @return Returns Properties from the Builder.
     */
    public Properties wrap() {
        return this.properties;
    }

}

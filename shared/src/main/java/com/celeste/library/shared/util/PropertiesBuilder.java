package com.celeste.library.shared.util;

import java.util.Properties;

public final class PropertiesBuilder {

    private final Properties properties;

    /**
     * Creates a new Properties.
     */
    public PropertiesBuilder() {
        this.properties = new Properties();
    }

    /**
     * @param key Key of the value.
     * @param value Value that will be put on that key.
     *
     * @return PropertiesBuilder
     */
    public PropertiesBuilder with(final String key, final String value) {
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

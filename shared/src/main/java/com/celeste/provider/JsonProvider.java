package com.celeste.provider;

import com.celeste.exceptions.UnableToCreateConfigException;
import com.celeste.model.Configuration;
import com.celeste.model.type.ConfigurationType;
import com.celeste.provider.impl.ConfigurationProvider;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import org.json.JSONPointerException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class JsonProvider extends Configuration implements ConfigurationProvider {

  private JSONObject jsonObject;

  public JsonProvider(final String name, final String path) {
    super(name, path, ConfigurationType.JSON);

    try {
      create(name, path);
    } catch (UnableToCreateConfigException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public Configuration create(final String name, final String path) throws UnableToCreateConfigException {
    final File file = new File(name + ".json");

    try {
      file.createNewFile();
      this.jsonObject = new JSONObject(Files.readAllLines(Paths.get(path)));

      return Configuration.builder()
          .name(name)
          .type(ConfigurationType.JSON)
          .path("src/main/resources")
          .build();
    } catch (IOException exception) {
      throw new UnableToCreateConfigException("Error while creating JSON configuration", exception);
    }
  }

  @Override @Nullable @SuppressWarnings("unchecked")
  public <T> T get(final String key) {
    if (jsonObject.get(key) == null) {
      throw new JSONPointerException("Unable to find object in json file");
    }

    return (T) jsonObject.get(key);
  }

}

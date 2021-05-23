package com.celeste.library.spigot.util.message.type;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClickEventType {

  OPEN_URL("open_url"),
  RUN_COMMAND("run_command"),
  SUGGEST_TEXT("suggest_command");

  private final String name;

  public static ClickEventType getEvent(final String event) {
    return Arrays.stream(values())
        .filter(type -> type.getName().equalsIgnoreCase(event))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid event: " + event));
  }

}

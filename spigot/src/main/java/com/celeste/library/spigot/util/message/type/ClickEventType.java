package com.celeste.library.spigot.util.message.type;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public enum ClickEventType {

  OPEN_URL("open_url"),
  RUN_COMMAND("run_command"),
  SUGGEST_TEXT("suggest_command");

  private final String name;

  @Nullable
  public static ClickEventType getEvent(final String event) {
    return Arrays.stream(values())
        .filter(type -> type.getName().equalsIgnoreCase(event))
        .findFirst()
        .orElse(null);
  }

  public static ClickEventType getEventOrThrow(final String event) {
    return Arrays.stream(values())
        .filter(type -> type.getName().equalsIgnoreCase(event))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid event: " + event));
  }

  public static ClickEventType getEvent(final String event, @Nullable final ClickEventType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getName().equalsIgnoreCase(event))
        .findFirst()
        .orElse(orElse);
  }

}

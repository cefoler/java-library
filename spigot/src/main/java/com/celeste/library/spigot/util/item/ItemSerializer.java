package com.celeste.library.spigot.util.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class ItemSerializer {

  /**
   * Serializes those items into bytes
   * @param items Itemstack...
   *
   * @return String
   * @throws IOException Throws if it wasn't able to put the items into byte
   */
  @NotNull
  public static String serialize(@NotNull final ItemStack... items) throws IOException {
    try (
        ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();
        BukkitObjectOutputStream objectOutput = new BukkitObjectOutputStream(arrayOutput)
    ) {
      objectOutput.writeInt(items.length);
      for (final ItemStack item : items) {
        objectOutput.writeObject(item);
      }

      return Base64Coder.encodeLines(arrayOutput.toByteArray());
    }
  }

  /**
   * Deserializes the items into ItemStack again
   * @param serializedItems String
   *
   * @return ItemStack[]
   *
   * @throws IOException Throws if a error occurs during deserialization
   * @throws ClassNotFoundException Throws when it wasn't able to find the class
   */
  @NotNull
  public static ItemStack[] deserialize(@NotNull final String serializedItems) throws IOException, ClassNotFoundException {
    try (
        ByteArrayInputStream arrayInput = new ByteArrayInputStream(Base64Coder.decodeLines(serializedItems));
        BukkitObjectInputStream objectInput = new BukkitObjectInputStream(arrayInput)
    ) {
      final ItemStack[] items = new ItemStack[objectInput.readInt()];
      for (int i = 0; i < items.length; i++) {
        items[i] = (ItemStack) objectInput.readObject();
      }

      return items;
    }
  }

}

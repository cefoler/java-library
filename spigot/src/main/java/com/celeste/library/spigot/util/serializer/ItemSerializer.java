package com.celeste.library.spigot.util.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemSerializer {

  /**
   * Serializes those items into bytes
   *
   * @param items ItemStack...
   * @return String
   * @throws IOException Throws if it wasn't able to put the items into byte
   */
  public static String serialize(final ItemStack... items) throws IOException {
    try (
        final ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();
        final BukkitObjectOutputStream objectOutput = new BukkitObjectOutputStream(arrayOutput)
    ) {
      objectOutput.writeInt(items.length);

      for (final ItemStack item : items) {
        objectOutput.writeObject(item);
      }

      return Base64.getEncoder().encodeToString(arrayOutput.toByteArray());
    }
  }

  /**
   * Deserializes the items into ItemStack again
   *
   * @param serializedItems String
   * @return ItemStack[]
   * @throws IOException            Throws if a error occurs during deserialization
   * @throws ClassNotFoundException Throws when it wasn't able to find the class
   */
  public static ItemStack[] deserialize(final String serializedItems)
      throws IOException, ClassNotFoundException {
    final byte[] decodeItems = Base64.getDecoder().decode(serializedItems);

    try (
        final ByteArrayInputStream arrayInput = new ByteArrayInputStream(decodeItems);
        final BukkitObjectInputStream objectInput = new BukkitObjectInputStream(arrayInput)
    ) {
      final ItemStack[] items = new ItemStack[objectInput.readInt()];

      for (int index = 0; index < items.length; index++) {
        items[index] = (ItemStack) objectInput.readObject();
      }

      return items;
    }
  }

}

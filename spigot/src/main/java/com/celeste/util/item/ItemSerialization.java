package com.celeste.util.item;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder.decodeLines;
import static org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder.encodeLines;

public final class ItemSerialization {

    private ItemSerialization() {}

    /**
     * Serializes items into String
     *
     * @param items Array of ItemStacks
     * @return String
     */
    public static String serialize(final ItemStack[] items) {
        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {

            final BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(byteStream);

            bukkitStream.writeInt(items.length);
            for (final ItemStack item : items) {
                bukkitStream.writeObject(item);
            }

            return encodeLines(byteStream.toByteArray());

        } catch (IOException exception) {
            throw new UnsupportedOperationException("Error while deserializing an item. " + exception.getMessage());
        }
    }

    /**
     * Deserializes a String to Array of ItemStack.
     * @param data String
     * @return ItemStack[]
     */
    public static ItemStack[] deserialize(final String data) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(decodeLines(data))) {

            final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            final ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            return items;

        } catch (IOException | ClassNotFoundException exception) {
            throw new UnsupportedOperationException("Error while deserializing an item. " + exception.getMessage());
        }
    }

}

package com.celeste.util.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ItemBuilder implements Cloneable {

    private ItemStack itemStack;
    private ItemMeta itemMeta;

    /**
     * Creates a new ItemBuilder with Material
     *
     * @param material Material of the Item
     */
    public ItemBuilder(final Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Creates a new ItemBuilder with Material, Amount and Data
     *
     * @param material Material
     * @param amount int
     * @param data int
     */
    public ItemBuilder(final Material material, final int amount, final int data) {
        this.itemStack = new ItemStack(material, amount, (byte) data);
        this.itemMeta = itemStack.getItemMeta();
    }

    /**
     * Sets the current Item into another item
     *
     * @param otherItem ItemStack
     */
    public ItemBuilder(final ItemStack otherItem) {
        this.itemStack = otherItem;
        this.itemMeta = otherItem.getItemMeta();
    }

    /**
     * Creates a ItemBuilder with a ItemMeta different than the meta of
     * the ItemStack provided
     *
     * @param item ItemStack
     * @param meta ItemMeta
     */
    public ItemBuilder(final ItemStack item, final ItemMeta meta) {
        this.itemStack = item;
        this.itemMeta = meta;
    }

    /**
     * Sets the ItemStack of this ItemBuilder
     *
     * @param item ItemStack
     * @return ItemBuilder
     */
    public ItemBuilder item(final ItemStack item) {
        this.itemStack = item;
        return this;
    }

    /**
     * Sets the ItemMeta of the item
     *
     * @param itemMeta ItemMeta
     * @return ItemBuilder
     */
    public ItemBuilder itemMeta(final ItemMeta itemMeta) {
        this.itemMeta = itemMeta;
        return this;
    }

    /**
     * Sets the material of the item
     *
     * @param material Material
     * @return ItemBuilder
     */
    public ItemBuilder material(final Material material) {
        itemStack.setType(material);
        return this;
    }

    /**
     * Sets the name of the item
     *
     * @param name String
     * @return ItemBuilder
     */
    public ItemBuilder name(final String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    /**
     * Sets the amount of the item
     *
     * @param amount int
     * @return ItemBuilder
     */
    public ItemBuilder amount(final int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    /**
     * Sets the lore
     *
     * @param lore String...
     * @return ItemBuilder
     */
    public ItemBuilder lore(final String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    /**
     * Sets the lore
     *
     * @param lore List
     * @return ItemBuilder
     */
    public ItemBuilder lore(final List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    /**
     * Adds multiple lores.
     * @param line String...
     * @return ItemBuilder
     */
    public ItemBuilder addLoreLine(final String... line) {
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.addAll(Arrays.asList(line));
        itemMeta.setLore(lore);
        return this;
    }


    /**
     * Sets durability of the item
     *
     * @param durability short
     * @return ItemBuilder
     */
    public ItemBuilder durability(final short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    /**
     * Adds enchantment to the item.
     *
     * @param enchantment Enchantment
     * @param level int
     *
     * @return ItemBuilder
     */
    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    /**
     * Add multiple enchantments by a String formatted in:
     * "ENCHANTMENT_NAME:LEVEL"
     *
     * @param enchantments List<String>
     * @return ItemBuilder
     */
    public ItemBuilder enchantments(final List<String> enchantments) {
        if (enchantments == null) {
            return this;
        }

        for (final String enchant : enchantments) {
            final String[] splitEnchant = enchant.split(":");
            final Enchantment enchantment = Enchantments.getEnchant(splitEnchant[0]);

            if (enchantment == null) {
                continue;
            }

            final int level = Integer.parseInt(splitEnchant[1]);
            itemStack.addUnsafeEnchantment(enchantment, level);
        }

        return this;
    }

    /**
     * Adds flags into the Item
     *
     * @param flags ItemFlag
     * @return ItemBuilder
     */
    public ItemBuilder addFlags(final ItemFlag... flags) {
        itemMeta.addItemFlags(flags);
        return this;
    }

    /**
     * Hide enchantments of the item
     * @return ItemBuilder
     */
    public ItemBuilder hideEnchantments() {
        addFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    /**
     *
     * @param owner String of player name
     * @return ItemBuilder
     */
    public ItemBuilder skull(final String owner) {
        final SkullMeta skull = (SkullMeta) itemMeta;

        itemStack.setDurability((short) 3);
        skull.setOwner(owner);

        return this;
    }

    /**
     * Clone the ItemBuilder
     *
     * @return ItemBuilder
     */
    public ItemBuilder clone() {
        return new ItemBuilder(itemStack.clone(), itemMeta.clone());
    }

    /**
     * Builds the ItemBuilder into ItemStack.
     *
     * @return ItemStack
     */
    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}

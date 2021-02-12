package com.celeste.menu;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Getter
public class Menu {

    private final String title;
    private final int size;

    private final MenuItem[] items;

    public Menu(String title, int size) {
        this.title = title;
        this.size = size;
        this.items = new MenuItem[size];
    }

    public final MenuItem slot(int slot, ItemStack item) {
        final MenuItem menuItem = new MenuItem(slot).withItem(item);

        items[slot] = menuItem;

        return menuItem;
    }

    public final MenuItem slot(int slot) {
        final MenuItem menuItem = new MenuItem(slot);

        items[slot] = menuItem;

        return menuItem;
    }

    protected void onRender(Player player, MenuHolder holder) { }

    protected void onOpen(InventoryOpenEvent event, MenuHolder holder) { }

    protected void onClose(InventoryCloseEvent event, MenuHolder holder) { }

    public final MenuHolder show(Player player) {
        return show(player, ImmutableMap.of());
    }

    public final MenuHolder show(Player player, Map<String, Object> properties) {
        final MenuHolder holder = new MenuHolder(this, properties);
        holder.show(player);

        return holder;
    }

}

package com.celeste.library.spigot.model.menu;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.Inventory;

import java.util.List;

@Data
@Builder
public final class MenuInventory {

    private final Inventory menu;
    private List<MenuItem> items;

}

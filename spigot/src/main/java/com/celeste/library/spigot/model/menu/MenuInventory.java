package com.celeste.library.spigot.model.menu;

import lombok.Builder;
import lombok.Data;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;

@Data
@Builder
public final class MenuInventory {

    private final Inventory menu;
    private ArrayList<MenuItem> items;

}

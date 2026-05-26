
package com.github.mcshanshuo.qimimaid.listeners;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import com.github.mcshanshuo.qimimaid.gui.CategoryGUI;
import com.github.mcshanshuo.qimimaid.gui.RecycleGUI;
import com.github.mcshanshuo.qimimaid.gui.TrashCanGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GUIListener implements Listener {

    private final QimiMaid plugin;
    private final Map<UUID, RecycleGUI> recycleGUIs;
    private final Map<UUID, CategoryGUI> categoryGUIs;
    private final Map<UUID, TrashCanGUI> trashCanGUIs;

    public GUIListener(QimiMaid plugin) {
        this.plugin = plugin;
        this.recycleGUIs = new HashMap<>();
        this.categoryGUIs = new HashMap<>();
        this.trashCanGUIs = new HashMap<>();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }

        Inventory inventory = event.getInventory();

        if (inventory instanceof org.bukkit.inventory.PlayerInventory) {
            return;
        }

        String title = "";
        try {
            title = inventory.getHolder() != null ? inventory.getHolder().getClass().getSimpleName() : "";
        } catch (Exception e) {
            title = "";
        }

        String viewTitle = event.getView().getTitle();
        if (viewTitle.contains("回收站")) {
            if (viewTitle.contains("分类")) {
                CategoryGUI categoryGUI = categoryGUIs.get(player.getUniqueId());
                if (categoryGUI != null) {
                    categoryGUI.handleClick(event);
                }
            } else {
                RecycleGUI recycleGUI = recycleGUIs.get(player.getUniqueId());
                if (recycleGUI != null) {
                    recycleGUI.handleClick(event);
                }
            }
        } else if (viewTitle.contains("垃圾桶")) {
            TrashCanGUI trashCanGUI = trashCanGUIs.get(player.getUniqueId());
            if (trashCanGUI != null) {
                trashCanGUI.handleClick(event);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        recycleGUIs.remove(player.getUniqueId());
        categoryGUIs.remove(player.getUniqueId());
        trashCanGUIs.remove(player.getUniqueId());
    }

    public void addRecycleGUI(Player player, RecycleGUI gui) {
        recycleGUIs.put(player.getUniqueId(), gui);
    }

    public void addCategoryGUI(Player player, CategoryGUI gui) {
        categoryGUIs.put(player.getUniqueId(), gui);
    }

    public void addTrashCanGUI(Player player, TrashCanGUI gui) {
        trashCanGUIs.put(player.getUniqueId(), gui);
    }
}


package com.github.mcshanshuo.qimimaid.gui;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrashCanGUI {

    private final QimiMaid plugin;
    private final Player player;
    private final Inventory inventory;

    public TrashCanGUI(QimiMaid plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        String title = plugin.getConfigManager().getTrashCanTitle();
        this.inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', title));
        registerGUI();
        populateInventory();
        player.openInventory(inventory);
    }

    private void registerGUI() {
        org.bukkit.plugin.PluginManager pm = plugin.getServer().getPluginManager();
        for (org.bukkit.event.Listener listener : pm.getPluginListeners(plugin)) {
            if (listener instanceof com.github.mcshanshuo.qimimaid.listeners.GUIListener) {
                ((com.github.mcshanshuo.qimimaid.listeners.GUIListener) listener).addTrashCanGUI(player, this);
                break;
            }
        }
    }

    private void populateInventory() {
        for (int i = 0; i < 26; i++) {
            inventory.setItem(i, createEmptySlot());
        }
        
        inventory.setItem(26, createClearButton());
    }

    private ItemStack createEmptySlot() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createClearButton() {
        ItemStack item = new ItemStack(Material.LAVA_BUCKET);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c清空垃圾桶"));
        
        List<String> lore = new ArrayList<>();
        lore.add("点击清空垃圾桶内所有物品");
        meta.setLore(lore);
        
        item.setItemMeta(meta);
        return item;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        
        int slot = event.getSlot();
        
        if (slot == 26) {
            clearInventory();
            plugin.getMessageManager().sendMessage(player, "trashcan.cleared");
            populateInventory();
            return;
        }
        
        if (slot >= 0 && slot < 26) {
            ItemStack item = event.getCurrentItem();
            if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                plugin.getRecycleManager().addItemStack(item, "normal");
                inventory.setItem(slot, createEmptySlot());
            }
        }
    }

    private void clearInventory() {
        for (int i = 0; i < 26; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null && item.getType() != Material.GRAY_STAINED_GLASS_PANE) {
                plugin.getRecycleManager().addItemStack(item, "normal");
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}

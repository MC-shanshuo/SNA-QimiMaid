
package com.github.mcshanshuo.qimimaid.gui;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import com.github.mcshanshuo.qimimaid.model.RecycledItem;
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

public class CategoryGUI {

    private final QimiMaid plugin;
    private final Player player;
    private final Inventory inventory;

    private static final String[] CATEGORIES = {"normal", "void", "fire", "cactus", "mob-drop", "despawn"};
    private static final Material[] CATEGORY_ICONS = {
            Material.GRASS_BLOCK,
            Material.END_PORTAL_FRAME,
            Material.FIRE_CHARGE,
            Material.CACTUS,
            Material.SKELETON_SKULL,
            Material.CLOCK
    };

    public CategoryGUI(QimiMaid plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&b回收站分类"));
        registerGUI();
        populateInventory();
        player.openInventory(inventory);
    }

    private void registerGUI() {
        org.bukkit.plugin.PluginManager pm = plugin.getServer().getPluginManager();
        for (org.bukkit.event.Listener listener : pm.getPluginListeners(plugin)) {
            if (listener instanceof com.github.mcshanshuo.qimimaid.listeners.GUIListener) {
                ((com.github.mcshanshuo.qimimaid.listeners.GUIListener) listener).addCategoryGUI(player, this);
                break;
            }
        }
    }

    private void populateInventory() {
        for (int i = 0; i < CATEGORIES.length; i++) {
            String category = CATEGORIES[i];
            if (plugin.getConfigManager().isCategoryEnabled(category)) {
                int count = plugin.getRecycleManager().getItemsByCategory(category).size();
                inventory.setItem(i, createCategoryItem(CATEGORY_ICONS[i], category, count));
            } else {
                inventory.setItem(i, createDisabledItem(category));
            }
        }

        inventory.setItem(22, createBackButton());
    }

    private ItemStack createCategoryItem(Material material, String category, int count) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        String name = plugin.getConfigManager().getCategoryName(category);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        
        List<String> lore = new ArrayList<>();
        lore.add("物品数量: " + count);
        lore.add("点击查看此分类");
        meta.setLore(lore);
        
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createDisabledItem(String category) {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        
        String name = plugin.getConfigManager().getCategoryName(category);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c" + name));
        
        List<String> lore = new ArrayList<>();
        lore.add("此分类已禁用");
        meta.setLore(lore);
        
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createBackButton() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().get("gui.back")));
        
        List<String> lore = new ArrayList<>();
        lore.add("返回回收站主界面");
        meta.setLore(lore);
        
        item.setItemMeta(meta);
        return item;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        
        int slot = event.getSlot();
        
        if (slot == 22) {
            new RecycleGUI(plugin, player);
            return;
        }
        
        if (slot >= 0 && slot < CATEGORIES.length) {
            String category = CATEGORIES[slot];
            if (plugin.getConfigManager().isCategoryEnabled(category)) {
                List<RecycledItem> filteredItems = plugin.getRecycleManager().getItemsByCategory(category);
                if (filteredItems.isEmpty()) {
                    plugin.getMessageManager().sendMessage(player, "recycle.empty");
                } else {
                    new RecycleGUI(plugin, player, filteredItems);
                }
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}

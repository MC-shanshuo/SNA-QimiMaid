
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

import java.util.*;

public class RecycleGUI {

    private final QimiMaid plugin;
    private final Player player;
    private int currentPage;
    private final List<RecycledItem> items;
    private Inventory inventory;

    public RecycleGUI(QimiMaid plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.currentPage = 1;
        this.items = new ArrayList<>(plugin.getRecycleManager().getItems());
        openGUI();
    }

    public RecycleGUI(QimiMaid plugin, Player player, List<RecycledItem> filteredItems) {
        this.plugin = plugin;
        this.player = player;
        this.currentPage = 1;
        this.items = filteredItems;
        openGUI();
    }

    private void openGUI() {
        int totalItems = items.size();
        int itemsPerPage = plugin.getConfigManager().getItemsPerPage();
        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);

        String title = plugin.getConfigManager().getGUITitle().replace("%page%", String.valueOf(currentPage));
        inventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', title));

        int startIndex = (currentPage - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, totalItems);

        for (int i = startIndex; i < endIndex; i++) {
            RecycledItem recycledItem = items.get(i);
            inventory.setItem(i - startIndex, createItemStack(recycledItem));
        }

        for (int i = endIndex - startIndex; i < 45; i++) {
            inventory.setItem(i, createEmptySlot());
        }

        if (currentPage > 1) {
            inventory.setItem(45, createPreviousPageItem());
        } else {
            inventory.setItem(45, createEmptySlot());
        }

        inventory.setItem(49, createCloseItem());

        if (currentPage < totalPages) {
            inventory.setItem(53, createNextPageItem());
        } else {
            inventory.setItem(53, createEmptySlot());
        }

        inventory.setItem(48, createCategoryButton());

        player.openInventory(inventory);
    }

    private ItemStack createItemStack(RecycledItem recycledItem) {
        ItemStack itemStack = recycledItem.getItemStack().clone();
        ItemMeta meta = itemStack.getItemMeta();
        
        List<String> lore = new ArrayList<>();
        lore.add(plugin.getMessageManager().get("gui.price").replace("%price%", String.valueOf(recycledItem.getPrice())));
        lore.add(plugin.getMessageManager().get("gui.origin").replace("%origin%", getCategoryName(recycledItem.getCategory())));
        lore.add(plugin.getMessageManager().get("gui.amount").replace("%amount%", String.valueOf(recycledItem.getAmount())));
        
        if (recycledItem.isValuable()) {
            lore.add(plugin.getMessageManager().get("gui.valuable"));
        }
        
        meta.setLore(lore);
        itemStack.setItemMeta(meta);
        
        return itemStack;
    }

    private String getCategoryName(String category) {
        return plugin.getConfigManager().getCategoryName(category);
    }

    private ItemStack createEmptySlot() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("");
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPreviousPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().get("gui.previous")));
        List<String> lore = new ArrayList<>();
        lore.add("点击前往上一页");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createNextPageItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().get("gui.next")));
        List<String> lore = new ArrayList<>();
        lore.add("点击前往下一页");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createCloseItem() {
        ItemStack item = new ItemStack(Material.BARRIER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().get("gui.close")));
        List<String> lore = new ArrayList<>();
        lore.add("点击关闭界面");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createCategoryButton() {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getMessageManager().get("gui.category")));
        List<String> lore = new ArrayList<>();
        lore.add("点击查看分类");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);
        
        int slot = event.getSlot();
        
        if (slot == 49) {
            player.closeInventory();
            return;
        }
        
        if (slot == 45 && currentPage > 1) {
            currentPage--;
            openGUI();
            return;
        }
        
        if (slot == 53) {
            int totalPages = (int) Math.ceil((double) items.size() / plugin.getConfigManager().getItemsPerPage());
            if (currentPage < totalPages) {
                currentPage++;
                openGUI();
            }
            return;
        }
        
        if (slot == 48) {
            new CategoryGUI(plugin, player);
            return;
        }
        
        if (slot < 45) {
            int itemIndex = (currentPage - 1) * plugin.getConfigManager().getItemsPerPage() + slot;
            if (itemIndex < items.size()) {
                RecycledItem recycledItem = items.get(itemIndex);
                handleItemPurchase(recycledItem);
            }
        }
    }

    private void handleItemPurchase(RecycledItem recycledItem) {
        if (!player.hasPermission("qimimaid.recycle.buy")) {
            plugin.getMessageManager().sendMessage(player, "no-permission");
            return;
        }

        if (!plugin.getEconomyManager().hasEnough(player, recycledItem.getPrice())) {
            plugin.getMessageManager().sendMessage(player, "recycle.buy-failed", 
                    Map.of("%price%", String.valueOf(recycledItem.getPrice())));
            return;
        }

        if (player.getInventory().firstEmpty() == -1) {
            plugin.getMessageManager().sendMessage(player, "recycle.inventory-full");
            return;
        }

        plugin.getEconomyManager().withdraw(player, recycledItem.getPrice());
        player.getInventory().addItem(recycledItem.getItemStack());
        plugin.getRecycleManager().removeItem(recycledItem.getId());

        plugin.getMessageManager().sendMessage(player, "recycle.buy-success",
                Map.of("%price%", String.valueOf(recycledItem.getPrice())));
        
        items.remove(recycledItem);
        if (items.isEmpty()) {
            player.closeInventory();
        } else {
            openGUI();
        }
    }

    public Inventory getInventory() {
        return inventory;
    }
}

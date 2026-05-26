
package com.github.mcshanshuo.qimimaid.managers;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import com.github.mcshanshuo.qimimaid.model.RecycledItem;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RecycleManager {

    private final QimiMaid plugin;
    private ConcurrentHashMap<UUID, RecycledItem> items;
    private BukkitRunnable autoCleanTask;
    private File dataFile;

    public RecycleManager(QimiMaid plugin) {
        this.plugin = plugin;
        this.items = new ConcurrentHashMap<>();
        this.dataFile = new File(plugin.getDataFolder(), "recycle.bin");
        loadData();
        startAutoCleanTask();
    }

    public void addItem(Item item, String category) {
        if (!plugin.getConfigManager().isRecycleEnabled()) {
            return;
        }

        if (!plugin.getConfigManager().isCategoryEnabled(category)) {
            return;
        }

        ItemStack itemStack = item.getItemStack().clone();
        boolean valuable = plugin.getConfigManager().isValuableItem(itemStack.getType().name());
        double price = calculatePrice(itemStack, valuable);

        RecycledItem recycledItem = new RecycledItem(itemStack, category, valuable, price);
        items.put(recycledItem.getId(), recycledItem);

        if (items.size() > plugin.getConfigManager().getMaxItems()) {
            removeOldestItems(items.size() - plugin.getConfigManager().getMaxItems());
        }
    }

    public void addItemStack(ItemStack itemStack, String category) {
        if (!plugin.getConfigManager().isRecycleEnabled()) {
            return;
        }

        if (!plugin.getConfigManager().isCategoryEnabled(category)) {
            return;
        }

        boolean valuable = plugin.getConfigManager().isValuableItem(itemStack.getType().name());
        double price = calculatePrice(itemStack, valuable);

        RecycledItem recycledItem = new RecycledItem(itemStack, category, valuable, price);
        items.put(recycledItem.getId(), recycledItem);

        if (items.size() > plugin.getConfigManager().getMaxItems()) {
            removeOldestItems(items.size() - plugin.getConfigManager().getMaxItems());
        }
    }

    private double calculatePrice(ItemStack itemStack, boolean valuable) {
        double basePrice = itemStack.getType().getMaxStackSize() * 0.1;
        double multiplier = plugin.getConfigManager().getPriceMultiplier();
        
        if (valuable) {
            multiplier *= plugin.getConfigManager().getValuableMultiplier();
        }

        return Math.round(basePrice * multiplier * itemStack.getAmount() * 100.0) / 100.0;
    }

    public boolean buyItem(UUID itemId, org.bukkit.entity.Player player) {
        RecycledItem item = items.get(itemId);
        if (item == null) {
            return false;
        }

        if (!plugin.getEconomyManager().hasEnough(player, item.getPrice())) {
            return false;
        }

        if (player.getInventory().firstEmpty() == -1) {
            return false;
        }

        plugin.getEconomyManager().withdraw(player, item.getPrice());
        player.getInventory().addItem(item.getItemStack());
        items.remove(itemId);
        
        return true;
    }

    public void removeItem(UUID itemId) {
        items.remove(itemId);
    }

    public void removeOldestItems(int count) {
        List<RecycledItem> sortedItems = new ArrayList<>(items.values());
        sortedItems.sort((a, b) -> Long.compare(a.getTimestamp(), b.getTimestamp()));

        for (int i = 0; i < Math.min(count, sortedItems.size()); i++) {
            items.remove(sortedItems.get(i).getId());
        }
    }

    public void clearAll() {
        items.clear();
    }

    public List<RecycledItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public List<RecycledItem> getItemsByCategory(String category) {
        return items.values().stream()
                .filter(item -> item.getCategory().equals(category))
                .toList();
    }

    public int getCount() {
        return items.size();
    }

    public RecycledItem getItem(UUID id) {
        return items.get(id);
    }

    private void startAutoCleanTask() {
        int interval = plugin.getConfigManager().getAutoCleanInterval() * 20;
        
        autoCleanTask = new BukkitRunnable() {
            @Override
            public void run() {
                clearAll();
                org.bukkit.Bukkit.broadcastMessage(plugin.getMessageManager().get("recycle.auto-clean"));
            }
        };

        autoCleanTask.runTaskTimer(plugin, interval, interval);
    }

    public void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            oos.writeObject(new ArrayList<>(items.values()));
        } catch (IOException e) {
            plugin.getLogger().severe("保存回收站数据失败: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadData() {
        if (!dataFile.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            List<RecycledItem> savedItems = (List<RecycledItem>) ois.readObject();
            for (RecycledItem item : savedItems) {
                items.put(item.getId(), item);
            }
        } catch (IOException | ClassNotFoundException e) {
            plugin.getLogger().severe("加载回收站数据失败: " + e.getMessage());
        }
    }
}

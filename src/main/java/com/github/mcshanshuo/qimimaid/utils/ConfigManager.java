
package com.github.mcshanshuo.qimimaid.utils;

import com.github.mcshanshuo.qimimaid.QimiMaid;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    private final QimiMaid plugin;
    private FileConfiguration config;

    public ConfigManager(QimiMaid plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    public void reload() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public int getCleanInterval() {
        return config.getInt("clean.interval", 300);
    }

    public int getWarningTime() {
        return config.getInt("clean.warning-time", 30);
    }

    public boolean isCleanEnabled() {
        return config.getBoolean("clean.enabled", true);
    }

    public boolean isMobCleanEnabled() {
        return config.getBoolean("mobs.enabled", true);
    }

    public List<String> getMobTypes() {
        return config.getStringList("mobs.types");
    }

    public boolean isRecycleEnabled() {
        return config.getBoolean("recycle.enabled", true);
    }

    public int getAutoCleanInterval() {
        return config.getInt("recycle.auto-clean-interval", 86400);
    }

    public int getMaxItems() {
        return config.getInt("recycle.max-items", 10000);
    }

    public double getPriceMultiplier() {
        return config.getDouble("recycle.price-multiplier", 0.5);
    }

    public double getValuableMultiplier() {
        return config.getDouble("recycle.valuable-multiplier", 2.0);
    }

    public List<String> getValuableItems() {
        return config.getStringList("recycle.valuable-items");
    }

    public boolean isCategoryEnabled(String category) {
        return config.getBoolean("categories." + category + ".enabled", true);
    }

    public String getCategoryName(String category) {
        return config.getString("categories." + category + ".name", category);
    }

    public String getGUITitle() {
        return config.getString("gui.title", "&b回收站 - 第 %page% 页");
    }

    public int getGUIRows() {
        return config.getInt("gui.rows", 6);
    }

    public int getItemsPerPage() {
        return config.getInt("gui.items-per-page", 45);
    }

    public String getTrashCanTitle() {
        return config.getString("gui.trashcan-title", "&c垃圾桶");
    }

    public boolean isValuableItem(String materialName) {
        return getValuableItems().contains(materialName.toUpperCase());
    }
}

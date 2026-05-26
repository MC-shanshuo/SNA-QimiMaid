
package com.github.mcshanshuo.qimimaid;

import com.github.mcshanshuo.qimimaid.commands.QimiMaidCommand;
import com.github.mcshanshuo.qimimaid.commands.TrashCanCommand;
import com.github.mcshanshuo.qimimaid.commands.RecycleCommand;
import com.github.mcshanshuo.qimimaid.listeners.EntityDeathListener;
import com.github.mcshanshuo.qimimaid.listeners.EntityRemoveListener;
import com.github.mcshanshuo.qimimaid.listeners.ItemDespawnListener;
import com.github.mcshanshuo.qimimaid.listeners.VoidListener;
import com.github.mcshanshuo.qimimaid.listeners.GUIListener;
import com.github.mcshanshuo.qimimaid.managers.CleanManager;
import com.github.mcshanshuo.qimimaid.managers.RecycleManager;
import com.github.mcshanshuo.qimimaid.managers.EconomyManager;
import com.github.mcshanshuo.qimimaid.utils.ConfigManager;
import com.github.mcshanshuo.qimimaid.utils.MessageManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class QimiMaid extends JavaPlugin {

    private static QimiMaid instance;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private EconomyManager economyManager;
    private RecycleManager recycleManager;
    private CleanManager cleanManager;

    @Override
    public void onEnable() {
        instance = this;
        
        saveDefaultConfig();
        
        configManager = new ConfigManager(this);
        messageManager = new MessageManager(this);
        economyManager = new EconomyManager(this);
        
        if (!economyManager.setupEconomy()) {
            getLogger().severe("Vault 插件未找到或经济插件未安装！插件将被禁用。");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        recycleManager = new RecycleManager(this);
        cleanManager = new CleanManager(this);
        
        registerListeners();
        registerCommands();
        
        getLogger().info("QimiMaid 扫地娘插件已成功加载！");
    }

    @Override
    public void onDisable() {
        if (cleanManager != null) {
            cleanManager.stopTasks();
        }
        if (recycleManager != null) {
            recycleManager.saveData();
        }
        getLogger().info("QimiMaid 扫地娘插件已卸载！");
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EntityDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityRemoveListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemDespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new VoidListener(this), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
    }

    private void registerCommands() {
        getCommand("qimimaid").setExecutor(new QimiMaidCommand(this));
        getCommand("trashcan").setExecutor(new TrashCanCommand(this));
        getCommand("recycle").setExecutor(new RecycleCommand(this));
    }

    public static QimiMaid getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public RecycleManager getRecycleManager() {
        return recycleManager;
    }

    public CleanManager getCleanManager() {
        return cleanManager;
    }
}
